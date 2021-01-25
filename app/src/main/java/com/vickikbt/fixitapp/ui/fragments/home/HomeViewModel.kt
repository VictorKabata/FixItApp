package com.vickikbt.fixitapp.ui.fragments.home

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

class HomeViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel() {

    var stateListener: StateListener? = null

    private val _postMutableLiveData = MutableLiveData<MutableList<Post>>()
    val posts: LiveData<MutableList<Post>> = _postMutableLiveData

    init {
        getAllPosts()
    }

    private fun getAllPosts() {
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

    fun fetchAllPosts() = liveData { emit(postRepository.fetchAllPosts()) }
}