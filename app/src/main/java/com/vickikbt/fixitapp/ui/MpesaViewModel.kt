package com.vickikbt.fixitapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vickikbt.fixitapp.repositories.MpesaRepository
import com.vickikbt.fixitapp.utils.Constants.INTERNET_MESSAGE
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MpesaViewModel @ViewModelInject constructor(private val mpesaRepository: MpesaRepository) :
    ViewModel() {

    private val accessTokenMutableLiveData = MutableLiveData<String>()

    //@Bindable
    //val phoneNumber: LiveData<String>? = null

    //@Bindable
    //val amount: LiveData<String>? = null

    var stateListener: StateListener? = null

    init {
        getAccessToken()
    }

    private fun getAccessToken() {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                val accessTokenResponse = mpesaRepository.getAccessToken()
                accessTokenMutableLiveData.value = accessTokenResponse.accessToken

                stateListener?.onSuccess(accessTokenResponse.accessToken)
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure(INTERNET_MESSAGE)
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

    fun makePayment(phoneNumber: String, amount: String) {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                val token = accessTokenMutableLiveData.value!!
                mpesaRepository.makePayment(token, phoneNumber, amount)
                stateListener?.onSuccess("Payed: $amount to $phoneNumber")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure(INTERNET_MESSAGE)
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }


}
