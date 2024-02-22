package com.example.zd5.DBUniversity

import android.provider.BaseColumns

object DBContactUniversity {

    class UniversityEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "universityDB"
            val TITLE = "title"
            val WEB = "web"
            val COUNTRY = "country"
        }
    }
}