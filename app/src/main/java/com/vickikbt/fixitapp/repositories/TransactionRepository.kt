package com.vickikbt.fixitapp.repositories

import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.models.network.TransactionRequest
import com.vickikbt.fixitapp.models.network.TransactionResponse
import com.vickikbt.fixitapp.utils.SafeApiRequest
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : SafeApiRequest() {

    suspend fun getUserTransactions(): List<TransactionResponse> {
        val user = appDatabase.userDAO().getAuthenticatedUser()
        val userId = user.id
        val token = "Bearer: ${user.token}"

        return safeApiRequest { apiService.getUserTransactions(token, userId) }
    }

    suspend fun createTransaction(
        postId: Int,
        amount: String,
        type: String,
        workId: Int,
        workerId: Int
    ): TransactionResponse {
        val user = appDatabase.userDAO().getAuthenticatedUser()
        val userId = user.id
        val token = "Bearer: ${user.token}"
        val transactionRequest = TransactionRequest(amount, postId, type, userId, workId, workerId)

        return safeApiRequest { apiService.createTransaction(token, transactionRequest) }
    }

}