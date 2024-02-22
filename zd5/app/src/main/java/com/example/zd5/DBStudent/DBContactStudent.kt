package com.example.zd5.DBStudent

import android.provider.BaseColumns

object DBContactStudent {

    class StudentEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "studentDB"
            val LOGIN = "login"
            val PASSWORD = "password"
            val EMAIL = "email"
            val FULLNAME = "fullname"
            val DATE = "date"
            val POINT = "point"
            val SPEC = "spec"
        }
    }
}