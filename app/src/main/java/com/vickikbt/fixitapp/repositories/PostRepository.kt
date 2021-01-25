package com.vickikbt.fixitapp.repositories

import androidx.lifecycle.MutableLiveData
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.data.preferences.TimePreference
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.models.network.UploadPostRequest
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.Coroutines
import com.vickikbt.fixitapp.utils.SafeApiRequest
import com.vickikbt.fixitapp.utils.TimeKeeper
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val timePreference: TimePreference
) : SafeApiRequest() {

    private val postMutableLiveData = MutableLiveData<List<Post>>()

    init {
        postMutableLiveData.observeForever { posts ->
            saveAllPosts(posts)
        }
    }

    private fun saveAllPosts(posts: List<Post>) =
        Coroutines.io { appDatabase.postDao().saveAllPosts(posts) }

    suspend fun getAllPosts(): Flow<MutableList<Post>> {
        val isPostCacheAvailable = appDatabase.postDao().isPostCacheAvailable() > 0
        val lastSyncTime = timePreference.getLastPostSyncTime
        val isTimeSurpassed = TimeKeeper.isTimeWithinInterval(
            Constants.TimeInterval,
            System.currentTimeMillis(),
            lastSyncTime
        )

        if (isPostCacheAvailable && !isTimeSurpassed) return appDatabase.postDao().getAllPosts()

        val postResponse = safeApiRequest { apiService.fetchAllPosts() }
        postMutableLiveData.value = postResponse
        timePreference.savePostSyncTime(System.currentTimeMillis())

        return appDatabase.postDao().getAllPosts()
    }

    fun getPost(id: Int) = appDatabase.postDao().getPost(id)

    suspend fun fetchAllPosts() {
        val postResponse = safeApiRequest { apiService.fetchAllPosts() }
        postMutableLiveData.value = postResponse
        timePreference.savePostSyncTime(System.currentTimeMillis())
    }

    suspend fun uploadPostPicture(body: MultipartBody.Part) =
        safeApiRequest { apiService.uploadPostPicture(body) }

    suspend fun uploadPost(
        category: String,
        description: String,
        imageURL: String,
        budget: String,
        latitude: Double,
        longitude: Double,
        address: String,
        region: String,
        country: String
    ): Post {
        val user = appDatabase.userDAO().getAuthenticatedUser()
        val token = user.token
        val userId = user.id

        val uploadPostRequestBody = UploadPostRequest(
            userId,
            category,
            description,
            imageURL,
            budget,
            latitude,
            longitude,
            address,
            region,
            country
        )

        return safeApiRequest { apiService.uploadPost(token!!, uploadPostRequestBody) }
    }

    suspend fun getUserPosts(): List<Post> {
        val userId = appDatabase.userDAO().getAuthenticatedUser().id
        return safeApiRequest { apiService.getUserPosts(userId) }
    }

    //suspend fun getPostBookings(postId:Int)=safeApiRequest { apiService.getPostBookings(postId) }


}