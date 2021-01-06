package com.vickikbt.fixitapp.utils

interface StateListener {

    fun onLoading()

    fun onSuccess(message:String)

    fun onFailure(message:String)

}