package com.example.zd5.DBCurrentUser

import android.provider.BaseColumns

object DBContactCurrentUser {

    class CurrentUserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "currentUserDB"
            val LOGIN = "login"
            val ROLE = "role"
        }
    }
}