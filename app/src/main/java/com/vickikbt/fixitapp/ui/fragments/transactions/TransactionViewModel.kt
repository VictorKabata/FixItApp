package com.vickikbt.fixitapp.ui.fragments.transactions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vickikbt.fixitapp.repositories.TransactionRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class TransactionViewModel @ViewModelInject constructor(private val transactionRepository: TransactionRepository) :
    ViewModel() {

    var stateListener: StateListener? = null

    fun getUserTransactions() = liveData {
        stateListener?.onLoading()

        try {
            val transactionResponse = transactionRepository.getUserTransactions()
            transactionResponse.let {
                emit(it)
                stateListener?.onSuccess("Fetched transactions")
            }
        } catch (e: ApiException) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        } catch (e: UnknownHostException) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        }

    }

    fun createTransaction(postId: Int, amount: String, type: String, workId: Int, workerId: Int) {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                val transactionRequest = transactionRepository.createTransaction(postId, amount, type, workId, workerId)
                stateListener?.onSuccess("Created transaction: ${transactionRequest.id}")
                return@launch
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

}