package com.example.zd5.DBTeacher

import android.provider.BaseColumns

object DBContactTeacher {

    class TeacherEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "teacherDB"
            val LOGIN = "login"
            val PASSWORD = "password"
            val EMAIL = "email"
            val FULLNAME = "fullname"
            val DATE = "date"
            val SPEC = "spec"
        }
    }
}