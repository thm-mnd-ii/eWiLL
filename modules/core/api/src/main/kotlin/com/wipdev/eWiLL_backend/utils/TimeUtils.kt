package com.wipdev.eWiLL_backend.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeUtils {
    companion object{
        fun getDateTimeString():String{
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return currentDateTime.format(formatter)
        }
    }
}