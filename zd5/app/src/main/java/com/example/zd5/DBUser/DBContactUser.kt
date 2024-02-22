package com.example.zd5.DBUser

import android.provider.BaseColumns

object DBContactUser {

    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "userDB"
            val LOGIN = "login"
            val PASSWORD = "password"
            val EMAIL = "email"
            val ROLE = "role"
        }
    }
}