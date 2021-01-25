package com.vickikbt.fixitapp.ui.fragments.post_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vickikbt.fixitapp.repositories.PostRepository
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.flow.collect

class PostDetailViewModel @ViewModelInject constructor(private val postRepository: PostRepository) :
    ViewModel() {

    var stateListener: StateListener? = null

    fun getPost(postId: Int) = liveData {
        stateListener?.onLoading()

        try {
            val post = postRepository.getPost(postId)
            post.collect { post ->
                stateListener?.onSuccess("Fetched post")
                emit(post)
            }
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        }
    }

}