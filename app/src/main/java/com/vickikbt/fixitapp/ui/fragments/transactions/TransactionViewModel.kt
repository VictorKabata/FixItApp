package com.vickikbt.fixitapp.ui.fragments.transactions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vickikbt.fixitapp.repositories.TransactionRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.StateListener
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

}