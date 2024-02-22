package com.example.zd5.DBSpecialty

import android.provider.BaseColumns

object DBContactSpecialty {
    class SpecialtyEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "specialtyDB"
            val TITLE = "title"
            val TYPE = "type"
            val LOAD = "load"
        }
    }
}