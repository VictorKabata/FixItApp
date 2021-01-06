package com.vickikbt.fixitapp.utils

import java.text.SimpleDateFormat

class DataFormatter {
    companion object {

        fun dateFormatter(date: String): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date)
            val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")

            val date = formatter.parse(simpleDateFormat.toString())

            return SimpleDateFormat("dd/MM/yyyy, hh:mma").format(date)
        }

    }

}