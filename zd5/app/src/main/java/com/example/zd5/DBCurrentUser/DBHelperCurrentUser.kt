package com.example.zd5.DBCurrentUser

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zd5.DBStudent.DBContactStudent
import com.example.zd5.DBStudent.Student
import com.example.zd5.DBUser.User

class DBHelperCurrentUser (val context: Context):
    SQLiteOpenHelper (context,DBContactCurrentUser.CurrentUserEntry.TABLE_NAME,  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val quary = "CREATE TABLE currentUserDB (id INT PRIMARY KEY, login TEXT, role TEXT)"
        db!!.execSQL(quary)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS currentUserDB")
        onCreate(db)
    }

    fun addCurrentUser(user: CurrentUser)
    {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("role", user.role)

        val db = this.writableDatabase
        db.insert("currentUserDB", null, values)
        db.close()
    }
    fun getRole(login: String, role: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM currentUserDB WHERE login = '$login' AND role = '$role'",null)
        return result.moveToFirst()
    }

    fun deleteCurrentUser(): Boolean {
        val db = writableDatabase
        db.delete("currentUserDB", null, null)
        return true
    }

    @SuppressLint("Range")
    fun getCurrentUser():ArrayList<CurrentUser> {
        val currentUser = ArrayList<CurrentUser>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor =
                db.rawQuery("select * from currentUserDB",
                    null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactCurrentUser.CurrentUserEntry.LOGIN)).toString()
                var role = cursor.getString(cursor.getColumnIndex(DBContactCurrentUser.CurrentUserEntry.ROLE)).toString()
                currentUser.add(CurrentUser(login, role))
                cursor.moveToNext()
            }
        }
        return currentUser
    }

}