package com.vickikbt.fixitapp.ui.fragments.work

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vickikbt.fixitapp.models.entity.Work
import com.vickikbt.fixitapp.repositories.BookingRepository
import com.vickikbt.fixitapp.repositories.UserRepository
import com.vickikbt.fixitapp.repositories.WorkRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class WorkViewModel @ViewModelInject constructor(
    private val workRepository: WorkRepository,
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository
) :
    ViewModel() {

    var stateListener: StateListener? = null

    fun getWork(postId: Int) = liveData {
        stateListener?.onLoading()

        try {
            val work = workRepository.getWork(postId)
            work.let {
                emit(it)
                stateListener?.onSuccess("Fetched work")
            }
            return@liveData

        } catch (e: ApiException) {
            //stateListener?.onFailure("${e.message}")
            return@liveData
        } catch (e: UnknownHostException) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        } catch (e: Exception) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        }
    }

    fun createWork(postId: Int, userId: Int) = liveData {
        stateListener?.onLoading()

        try {
            val work = workRepository.createWork(postId, workerId = userId)
            work.let {
                emit(it)
                stateListener?.onSuccess("Created work")
            }
            return@liveData
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

    fun updateWork(work: Work) = liveData {
        stateListener?.onLoading()

        try {
            val workUpdateResponse = workRepository.updateWork(work)
            workUpdateResponse.let { work ->
                bookingRepository.updateBookedPost(
                    work.postId,
                    work.workerId,
                    Constants.STATUS_COMPLETED,
                    false
                )
                emit(work)
                stateListener?.onSuccess("Updated work to complete")
                return@liveData
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

    fun reviewUser(work: Work, rating: Int, comment: String) {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                userRepository.reviewUser(work, rating, comment)
                //stateListener?.onSuccess("Rated ${work.worker.username} with $rating because: $comment")
                stateListener?.onSuccess("Rated")
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