package com.vickikbt.fixitapp.ui.fragments.auth

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.vickikbt.fixitapp.repositories.UserRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.NoInternetException
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.UnknownHostException

class UserViewModel @ViewModelInject constructor(private val userRepository: UserRepository) :
    ViewModel(), Observable {

    var stateListener: StateListener? = null

    @Bindable
    val userName = MutableLiveData<String>()

    @Bindable
    val emailAddress = MutableLiveData<String>()

    @Bindable
    val phoneNumber = MutableLiveData<String>()

    @Bindable
    val password = MutableLiveData<String>()

    //private val _reviewsMutableLiveData = MutableLiveData<List<Review>>()
    //val userReviews: LiveData<List<Review>> = _reviewsMutableLiveData

    val getCurrentUser = userRepository.getAuthenticatedUser().asLiveData()

    suspend fun getCurrentUserReviews() =userRepository.getCurrentUserReviews().asLiveData()

    fun loginUser(view: View) {
        stateListener?.onLoading()

        if (emailAddress.value.isNullOrEmpty()) {
            stateListener?.onFailure("Enter email address")
            return
        } else if (password.value.isNullOrEmpty()) {
            stateListener?.onFailure("Enter password")
            return
        }

        viewModelScope.launch {
            try {
                val authResponse = userRepository.loginUser(emailAddress.value!!, password.value!!)

                authResponse.user.let {
                    stateListener?.onSuccess("Welcome, ${it.username}")
                    userRepository.saveAuthenticatedUser(it)
                    userRepository.fetchCurrentUserReviews() //Fetch user reviews and save to sqlite
                    return@launch
                }
            } catch (e: ApiException) {
                stateListener?.onFailure(e.message.toString())
                return@launch
            } catch (e: NoInternetException) {
                stateListener?.onFailure(e.message.toString())
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("Login Failed")
                return@launch
            }
        }
    }

    fun registerUser(
        imageUrl: String,
        specialisation: String,
        latitude: Double,
        longitude: Double,
        address: String,
        region: String,
        country: String
    ) {
        stateListener?.onLoading()

        when {
            userName.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter username")
                return
            }
            emailAddress.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter email address")
                return
            }
            phoneNumber.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter phone number")
                return
            }
            password.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter password")
                return
            }
        }

        viewModelScope.launch {
            try {
                val authResponse = userRepository.registerUser(
                    userName.value!!,
                    emailAddress.value!!,
                    phoneNumber.value!!,
                    imageUrl,
                    specialisation,
                    password.value!!,
                    latitude,
                    longitude,
                    address,
                    region,
                    country
                )

                authResponse.user.let {
                    stateListener?.onSuccess("Welcome, ${it.username}")
                    userRepository.saveAuthenticatedUser(it)
                    return@launch
                }

            } catch (e: ApiException) {
                stateListener?.onFailure(e.message.toString())
                return@launch
            } catch (e: NoInternetException) {
                stateListener?.onFailure(e.message.toString())
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("Registration Failed")
                return@launch
            }
        }
    }

    fun uploadProfilePic(profilePicture: MultipartBody.Part) = liveData {
        stateListener?.onLoading()

        when {
            userName.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter username")
            }
            emailAddress.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter email address")
            }
            phoneNumber.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter phone number")
            }
            password.value.isNullOrEmpty() -> {
                stateListener?.onFailure("Enter password")
            }
        }

        try {
            val imageUrl = userRepository.uploadProfilePicture(profilePicture).imageURL
            emit(imageUrl)
        } catch (e: ApiException) {
            stateListener?.onFailure(e.message.toString())
            return@liveData
        } catch (e: NoInternetException) {
            stateListener?.onFailure(e.message.toString())
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure("Registration Failed")
            return@liveData
        }
    }

    fun fetchCurrentUserReviews() = viewModelScope.launch { userRepository.fetchCurrentUserReviews() }

    fun fetchUser(id: Int) = liveData {
        stateListener?.onLoading()

        try {
            val userResponse = userRepository.fetchUser(id)
            //fetchUserReviews(userResponse.id) //Fetch user reviews
            emit(userResponse)
            stateListener?.onSuccess("Fetched user details")
            return@liveData
        } catch (e: ApiException) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        } catch (e: UnknownHostException) {
            stateListener?.onFailure("Ensure you have an internet connection")
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure("Error loading user profile")
            return@liveData
        }
    }

    fun fetchUserReviews(userId: Int)= liveData {
        stateListener?.onLoading()

        try {
            val userReviewsResponse=userRepository.fetchUserReviews(userId)
            userReviewsResponse.let {reviews->
                //_reviewsMutableLiveData.value=reviews
                emit(reviews)
                stateListener?.onSuccess("Fetched user's reviews")
                return@liveData
            }
        }catch (e:ApiException){
            stateListener?.onFailure("${e.message}")
            return@liveData
        }catch (e:UnknownHostException){
            stateListener?.onFailure("Ensure you have an internet connection")
            return@liveData
        }catch (e:Exception){
            stateListener?.onFailure("Error loading users reviews")
            return@liveData
        }
    }

    fun logoutUser() = userRepository.logoutUser()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}