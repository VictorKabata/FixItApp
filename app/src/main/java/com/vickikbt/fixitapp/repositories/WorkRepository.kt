package com.vickikbt.fixitapp.repositories

import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.models.entity.Work
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

}