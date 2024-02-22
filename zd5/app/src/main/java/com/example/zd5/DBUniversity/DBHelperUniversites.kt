package com.example.zd5.DBUniversity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zd5.DBUser.DBContactUser

class DBHelperUniversites (val context: Context): SQLiteOpenHelper(context,
    DBContactUniversity.UniversityEntry.TABLE_NAME,  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val quary = "CREATE TABLE universityDB (id INT PRIMARY KEY, title TEXT, web TEXT, country TEXT)"
        db!!.execSQL(quary)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS universityDB")
        onCreate(db)
    }
    fun addUniversity(uni: University)
    {
        val values = ContentValues()
        values.put("title", uni.title)
        values.put("web", uni.web)
        values.put("country", uni.country)
        val db = this.writableDatabase
        db.insert(DBContactUniversity.UniversityEntry.TABLE_NAME, null, values)
        db.close()
    }
    @SuppressLint("Range")
    fun readAllUniversities(): ArrayList<University> {
        val universites = ArrayList<University>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from universityDB", null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        var title: String
        var web: String
        var country: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                title = cursor.getString(cursor.getColumnIndex(DBContactUniversity.UniversityEntry.TITLE))
                web = cursor.getString(cursor.getColumnIndex(DBContactUniversity.UniversityEntry.WEB))
                country = cursor.getString(cursor.getColumnIndex(DBContactUniversity.UniversityEntry.COUNTRY))
                universites.add(University(title,web,country))
                cursor.moveToNext()
            }
        }
        return universites
    }
}