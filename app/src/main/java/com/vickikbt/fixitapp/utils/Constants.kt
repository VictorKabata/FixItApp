package com.vickikbt.fixitapp.utils

object Constants {

    //const val BASE_URL: String = "https://fix-it-server.herokuapp.com/"
    //const val BASE_URL: String = "http://10.0.2.2:8081/"
    const val BASE_URL: String = "http://192.168.43.125:8081/"
    const val SAF_BASE_URL = "https://sandbox.safaricom.co.ke/"

    const val PERMISSION_ID = 69

    const val THEME_KEY = "theme"

    const val SHARED_PREF_NAME = "Time Keeper"

    const val POST_SHARED_PREF_KEY = "Post_Time"

    const val REVIEW_SHARED_PREF_KEY = "Review_Time"

    const val TimeInterval = 6L

    const val ACCEPT = "Accepted"
    const val REJECT = "Rejected"
    const val IN_PROGRESS = "In-Progress"
    const val COMPLETED="Completed"

    const val INTERNET_MESSAGE="Ensure you have an internet connection"

    //TODO: Move to config file or add constants to gitignore
    const val CONSUMER_KEY="bZwcaGor0sSwGqLPBAiRtLoXaPwIk8Gy"
    const val CONSUMER_SECRET="N3w5oMiHXu0ZLT8H"

    const val BUSINESS_SHORT_CODE = "174379"
    const val PASSKEY = "add_password"
    const val TRANSACTION_TYPE = "CustomerPayBillOnline"
    const val PARTYB = "174379"

    const val CALLBACKURL = "http://mpesa-requestbin.herokuapp.com/"
}