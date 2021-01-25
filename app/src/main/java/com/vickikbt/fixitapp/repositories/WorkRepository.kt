package com.vickikbt.fixitapp.repositories

import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.models.network.WorkRequest
import com.vickikbt.fixitapp.utils.SafeApiRequest
import javax.inject.Inject

class WorkRepository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {

    suspend fun getUserWorks(): List<Work> {
        val userId = appDatabase.userDAO().getAuthenticatedUser().id
        return safeApiRequest { apiService.getUserWorks(userId) }
    }

    suspend fun createWork(postId: Int, workerId: Int): Work {
        val user = appDatabase.userDAO().getAuthenticatedUser()

        val token = "Bearer: ${user.token}"
        val workRequest = WorkRequest(postId, user.id, workerId)

        return safeApiRequest { apiService.createWork(token, workRequest) }
    }

    suspend fun getWork(id: Int) = safeApiRequest { apiService.getWork(id) }

}