package com.vickikbt.fixitapp.ui.fragments.upload

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vickikbt.fixitapp.repositories.PostRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.NoInternetException
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.UnknownHostException

class UploadViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel(), Observable {

    var stateListener: StateListener? = null

    @Bindable
    val description = MutableLiveData<String>()

    @Bindable
    val budget = MutableLiveData<String>()

    fun uploadPostPicture(body: MultipartBody.Part) = liveData {
        stateListener?.onLoading()

        try {
            val photoUploadResponseBody = postRepository.uploadPostPicture(body)
            photoUploadResponseBody.imageURL.let {
                //stateListener?.onSuccess("Image Uploaded")
                emit(it)
                return@liveData
            }
        } catch (e: ApiException) {
            stateListener?.onFailure(e.message.toString())
            return@liveData
        } catch (e: NoInternetException) {
            stateListener?.onFailure(e.message.toString())
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure(e.message.toString())
            return@liveData
        }

    }

    fun uploadPost(
        category: String,
        imageURL: String,
        latitude: Double,
        longitude: Double,
        address: String,
        region: String,
        country: String
    ) {
        viewModelScope.launch {
            try {
                postRepository.uploadPost(
                    category,
                    description.value.toString(),
                    imageURL,
                    budget.value.toString(),
                    latitude,
                    longitude,
                    address,
                    region,
                    country
                )
                stateListener?.onSuccess("Uploaded")
            } catch (e: ApiException) {
                stateListener?.onFailure(e.message.toString())
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure("Ensure you have an internet connection")
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("Error uploading post")
                return@launch
            }
        }
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}