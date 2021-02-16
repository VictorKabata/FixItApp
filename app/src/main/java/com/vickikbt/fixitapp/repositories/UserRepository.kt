package com.vickikbt.fixitapp.repositories

import androidx.lifecycle.MutableLiveData
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.data.preferences.TimePreference
import com.vickikbt.fixitapp.models.entity.Review
import com.vickikbt.fixitapp.models.entity.User
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.models.network.*
import com.vickikbt.fixitapp.utils.Coroutines
import com.vickikbt.fixitapp.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val timePreference: TimePreference
) : SafeApiRequest() {

    private val reviewMutableLiveData = MutableLiveData<List<Review>>()

    init {
        reviewMutableLiveData.observeForever { reviews ->
            saveAllReviews(reviews)
        }
    }

    suspend fun saveAuthenticatedUser(user: User) =
        appDatabase.userDAO().saveAuthenticatedUser(user)

    fun getAuthenticatedUser() = flow { emit(appDatabase.userDAO().getAuthenticatedUser()) }

    fun logoutUser() = appDatabase.clearAllTables()

    suspend fun loginUser(email: String, password: String): AuthResponse {
        val loginRequestBody = LoginRequest(email, password)

        return safeApiRequest { apiService.userLogin(loginRequestBody) }
    }


    private fun saveAllReviews(reviews: List<Review>) =
        Coroutines.io { appDatabase.reviewDao().saveAllReviews(reviews) }

    suspend fun getCurrentUserReviews(): Flow<MutableList<Review>> {
        val isReviewCacheAvailable = appDatabase.reviewDao().isReviewCacheAvailable() > 0

        if (isReviewCacheAvailable) return appDatabase.reviewDao().getAllReviews()

        //TODO: Add check internet connectivity
        val currentUserId = appDatabase.userDAO().getAuthenticatedUser().id
        val userReviewsResponse = safeApiRequest { apiService.getUserReviews(currentUserId) }
        reviewMutableLiveData.value = userReviewsResponse
        timePreference.saveReviewSyncTime(System.currentTimeMillis())

        return appDatabase.reviewDao().getAllReviews()
    }

    suspend fun fetchCurrentUserReviews() {
        val userId = appDatabase.userDAO().getAuthenticatedUser().id
        val reviewsRequest = safeApiRequest { apiService.getUserReviews(userId) }
        reviewMutableLiveData.value = reviewsRequest
        timePreference.saveReviewSyncTime(System.currentTimeMillis())
    }

    suspend fun registerUser(
        username: String,
        email: String,
        phoneNumber: String,
        imageUrl: String,
        specialisation: String,
        password: String,
        latitude: Double,
        longitude: Double,
        region: String,
        address: String,
        country: String
    ): AuthResponse {
        val registrationRequestBody = RegistrationRequest(
            username,
            email,
            phoneNumber,
            imageUrl,
            specialisation,
            password,
            latitude,
            longitude,
            region,
            address,
            country
        )

        return safeApiRequest { apiService.userRegistration(registrationRequestBody) }
    }

    suspend fun uploadProfilePicture(profilePicture: MultipartBody.Part): PhotoUploadResponse {
        return safeApiRequest { apiService.uploadProfilePicture(profilePicture) }
    }

    suspend fun fetchUser(id: Int) = safeApiRequest { apiService.getUser(id) }

    suspend fun fetchUserReviews(userId: Int) = safeApiRequest { apiService.getUserReviews(userId) }

    suspend fun reviewUser(work:Work,rating:Int,comment:String){
        val reviewUserRequest=ReviewUserRequest(work.userId,work.workerId,comment, rating)
        val token="Bearer: ${appDatabase.userDAO().getAuthenticatedUser().token}"
        safeApiRequest { apiService.reviewUser(token, reviewUserRequest) }
    }

}