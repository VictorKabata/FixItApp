package com.vickikbt.fixitapp.ui.fragments.post_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vickikbt.fixitapp.repositories.BookingRepository
import com.vickikbt.fixitapp.repositories.PostRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PostDetailViewModel @ViewModelInject constructor(
    private val postRepository: PostRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    var stateListener: StateListener? = null

    fun getPost(postId: Int) = liveData {
        stateListener?.onLoading()

        try {
            val postResponse = postRepository.getPost(postId)
            postResponse.let { post ->
                stateListener?.onSuccess(null.toString())
                emit(post)
            }
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        }
    }

    fun bookWork(postId: Int, bid: String, comment: String) {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                bookingRepository.bookWork(postId, bid, comment)
                stateListener?.onSuccess("Work booked. Wait for approval.")
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure("Ensure you have an internet connection")
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

}