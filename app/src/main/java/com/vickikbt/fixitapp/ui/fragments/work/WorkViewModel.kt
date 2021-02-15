package com.vickikbt.fixitapp.ui.fragments.work

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vickikbt.fixitapp.repositories.WorkRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.StateListener
import java.net.UnknownHostException

class WorkViewModel @ViewModelInject constructor(private val workRepository: WorkRepository) :
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

    fun updateWork(workId:Int)= liveData {
        stateListener?.onLoading()

        try {
            val workUpdateResponse=workRepository.updateWork(workId)
            workUpdateResponse.let {work->
                emit(work)
                stateListener?.onSuccess("Updated work to complete")
                return@liveData
            }
        }catch (e:ApiException){
            stateListener?.onFailure("${e.message}")
            return@liveData
        }catch (e:UnknownHostException){
            stateListener?.onFailure("${e.message}")
            return@liveData
        }catch (e:Exception){
            stateListener?.onFailure("${e.message}")
            return@liveData
        }
    }
}