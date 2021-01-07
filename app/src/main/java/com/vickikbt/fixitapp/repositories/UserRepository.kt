package com.vickikbt.fixitapp.repositories

import androidx.lifecycle.MutableLiveData
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.data.preferences.TimePreference
import com.vickikbt.fixitapp.models.entity.Review
import com.vickikbt.fixitapp.models.entity.User
import com.vickikbt.fixitapp.models.network.AuthResponse
import com.vickikbt.fixitapp.models.network.LoginRequest
import com.vickikbt.fixitapp.models.network.PhotoUploadResponse
import com.vickikbt.fixitapp.models.network.RegistrationRequest
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.Coroutines
import com.vickikbt.fixitapp.utils.SafeApiRequest
import com.vickikbt.fixitapp.utils.TimeKeeper
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

    /*suspend fun getUserReviews(userId: Int): Flow<MutableList<Review>> {
        val lastSyncTime = timePreference.getLastReviewSyncTime
        val isTimeSurpassed = TimeKeeper.isTimeWithinInterval(
            Constants.TimeInterval,
            System.currentTimeMillis(),
            lastSyncTime
        )

        if (!isTimeSurpassed) return appDatabase.reviewDao().getAllReviews()

        val reviewsRequest = safeApiRequest { apiService.getUserReviews(userId) }
        reviewMutableLiveData.value = reviewsRequest
        timePreference.saveReviewSyncTime(System.currentTimeMillis())

        return appDatabase.reviewDao().getAllReviews()
    }*/

    fun getCurrentUserReviews()=appDatabase.reviewDao().getAllReviews()

    private fun saveAllReviews(reviews: List<Review>) =
        Coroutines.io { appDatabase.reviewDao().saveAllReviews(reviews) }

    suspend fun fetchCurrentUserReviews(userId:Int){
        val reviewsRequest=safeApiRequest { apiService.getUserReviews(userId) }
        reviewMutableLiveData.value=reviewsRequest
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

    suspend fun fetchUserReviews(userId: Int)=safeApiRequest { apiService.getUserReviews(userId) }

}