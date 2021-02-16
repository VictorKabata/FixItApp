package com.vickikbt.fixitapp.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class DataFormatter {
    companion object {

        //2021-01-26T15:33:30+03:00
        @SuppressLint("SimpleDateFormat")
        fun dateFormatter(date: String): String {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date)
            val targetFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a")

            return targetFormat.format(originalFormat)
        }

        //2021-02-16T12:08:45.0409368+03:00
        @SuppressLint("SimpleDateFormat")
        fun updateDateFormatter(date: String): String {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssssssZ").parse(date)
            val targetFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a")

            return targetFormat.format(originalFormat)
        }

        //0714091304-Input
        //+254714091304-Input
        //714091304-Output
        fun phoneNumberFormatter(phoneNumber:String): String {
            return when {
                phoneNumber.startsWith("0") -> {
                    phoneNumber.removePrefix("0")
                }
                phoneNumber.startsWith("+254") -> {
                    phoneNumber.removePrefix("+254")
                }
                else -> phoneNumber
            }
        }

    }

}