package com.vickikbt.fixitapp.ui.viewmodels

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.repositories.PostRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.NoInternetException
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.UnknownHostException

class PostViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel(), Observable {

    var stateListener: StateListener? = null

    @Bindable
    val description = MutableLiveData<String>()

    @Bindable
    val budget = MutableLiveData<String>()

    private val _postMutableLiveData = MutableLiveData<MutableList<Post>>()
    val posts: LiveData<MutableList<Post>> = _postMutableLiveData

    fun getAllPosts() {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                val postResponse = postRepository.getAllPosts()
                postResponse.collect { posts ->
                    Log.e("VickiKbt", "getAllPosts: $posts")
                    _postMutableLiveData.postValue(posts)// = posts
                    stateListener?.onSuccess("Fetched all posts")
                }
                return@launch
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure("Ensure you have internet connection")
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

    fun getPost(postId: Int) = postRepository.getPost(postId).asLiveData()

    fun fetchAllPosts() = liveData { emit(postRepository.fetchAllPosts()) }

    fun uploadPostPicture(body: MultipartBody.Part) = liveData {
        stateListener?.onLoading()

        try {
            val photoUploadResponseBody = postRepository.uploadPostPicture(body)
            photoUploadResponseBody.imageURL.let {
                stateListener?.onSuccess("Image Uploaded")
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