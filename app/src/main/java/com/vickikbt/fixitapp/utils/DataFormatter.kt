package com.vickikbt.fixitapp.utils

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

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
        fun phoneNumberFormatter(phoneNumber: String): String {
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

        //Cleans the entered phoneNumber to format accepted by DarajaAPI
        //254714091304-Output
        fun sanitizePhoneNumber(phoneNumber: String): String {
            if (phoneNumber == "") {
                return ""
            }
            if (phoneNumber.length < 11 && phoneNumber.startsWith("0")) {
                return phoneNumber.replaceFirst("^0".toRegex(), "254")
            }
            return if (phoneNumber.length == 13 && phoneNumber.startsWith("+")) {
                phoneNumber.replaceFirst("^+".toRegex(), "")
            } else phoneNumber
        }

        fun getTimeStamp(): String {
            val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
            Log.e("VickiKbt", "timeStamp: $timeStamp")
            return timeStamp
        }

        //Generates password for Daraja API
        fun getPassword(timeStamp:String, businessShortCode:String, passKey:String): String? {
            //val timestamp = getTimeStamp()
            //val businessShortCode = Constants.BUSINESS_SHORT_CODE
            //val passkey = Constants.PASSKEY

            val str = businessShortCode + passKey + timeStamp
            val encodedStr = Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP)
            Log.e("VickiKbt", "password text: $str")
            Log.e("VickiKbt", "password encoded: $encodedStr")

            return encodedStr
        }

    }

}