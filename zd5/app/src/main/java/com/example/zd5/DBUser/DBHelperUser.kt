package com.example.zd5.DBUser

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zd5.DBStudent.DBContactStudent
import com.example.zd5.DBStudent.Student

class DBHelperUser (val context: Context): SQLiteOpenHelper (context,DBContactUser.UserEntry.TABLE_NAME,  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val quary = "CREATE TABLE userDB (id INT PRIMARY KEY, login TEXT, password TEXT, email TEXT, role TEXT)"
        db!!.execSQL(quary)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS userDB")
        onCreate(db)
    }

    fun getUser(login: String, email: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM userDB WHERE login = '$login' AND email = '$email'",null)
        return result.moveToFirst()
    }

    fun getUser(login: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM userDB WHERE login = '$login'",null)
        return result.moveToFirst()
    }

    fun getUserRole(login: String, role: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM userDB WHERE login = '$login' AND role = '$role'",null)
        return result.moveToFirst()
    }

    fun addUser(user: User)
    {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("password", user.password)
        values.put("email", user.email)
        values.put("role", user.role)

        val db = this.writableDatabase
        db.insert("userDB", null, values)
        db.close()
    }
    fun deleteUserLogin(login: String): Boolean
    {
        val db = writableDatabase
        val selection = DBContactUser.UserEntry.LOGIN + " LIKE ?"
        val selectionArgs = arrayOf(login)
        db.delete(DBContactUser.UserEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }
    fun deleteAllRole(role: String): Boolean
    {
        val db = writableDatabase
        val selection = DBContactUser.UserEntry.ROLE + " LIKE ?"
        val selectionArgs = arrayOf(role)
        db.delete(DBContactUser.UserEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }


}