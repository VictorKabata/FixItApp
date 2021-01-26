package com.vickikbt.fixitapp.utils

import java.text.SimpleDateFormat

class DataFormatter {
    companion object {

        //2021-01-26T15:33:30+03:00
        fun dateFormatter(date: String): String {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date)
            val targetFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a")

            return targetFormat.format(originalFormat)
        }

    }

}