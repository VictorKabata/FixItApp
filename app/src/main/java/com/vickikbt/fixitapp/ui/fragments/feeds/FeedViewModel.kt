package com.vickikbt.fixitapp.ui.fragments.feeds

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.repositories.PostRepository
import com.vickikbt.fixitapp.repositories.WorkRepository
import com.vickikbt.fixitapp.utils.Constants.INTERNET
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class FeedViewModel @ViewModelInject constructor(
    private val postRepository: PostRepository,
    private val workRepository: WorkRepository
) :
    ViewModel() {

    var stateListener: StateListener? = null

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _works = MutableLiveData<List<Work>>()
    val works: LiveData<List<Work>> = _works

    init {
        getUserPosts()

        getUserWorks()
    }

    private fun getUserPosts() {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                val posts = postRepository.getUserPosts()
                _posts.value = posts
                stateListener?.onSuccess("Fetched")
                return@launch
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure(INTERNET)
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

    private fun getUserWorks() {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                val works = workRepository.getUserWorks()
                _works.value = works
                stateListener?.onSuccess("Fetched")
                return@launch
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure(INTERNET)
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

}