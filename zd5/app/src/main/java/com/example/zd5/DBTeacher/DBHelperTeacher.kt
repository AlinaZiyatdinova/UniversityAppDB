package com.example.zd5.DBTeacher

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zd5.DBStudent.DBContactStudent

class DBHelperTeacher (val context: Context):
    SQLiteOpenHelper (context,DBContactTeacher.TeacherEntry.TABLE_NAME,  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val quary = "CREATE TABLE teacherDB (id INT PRIMARY KEY, login TEXT, password TEXT, email TEXT, fullname TEXT, date TEXT, spec TEXT)"
        db!!.execSQL(quary)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS teacherDB")
        onCreate(db)
    }

    fun getTeacher(login: String, spec: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM teacherDB WHERE login = '$login' AND spec ='$spec'",null)
        return result.moveToFirst()
    }

    fun getTeacher(login: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM teacherDB WHERE login = '$login'",null)
        return result.moveToFirst()
    }

    fun addTeacher(teacher: Teacher)
    {
        val values = ContentValues()
        values.put("login", teacher.login)
        values.put("password", teacher.password)
        values.put("email", teacher.email)
        values.put("fullname", teacher.fullname)
        values.put("date", teacher.date)
        values.put("spec", teacher.spec)

        val db = this.writableDatabase
        db.insert("teacherDB", null, values)
        db.close()
    }

    fun deleteAllTeacher(): Boolean {
        val db = writableDatabase
        db.delete("teacherDB", null, null)
        return true
    }

    fun deleteTeacher(login: String, spec: String): Boolean
    {
        val db = writableDatabase
        val selection = "${DBContactTeacher.TeacherEntry.LOGIN} = ? AND ${DBContactTeacher.TeacherEntry.SPEC} = ?"
        val selectionArgs = arrayOf(login, spec)
        db.delete(DBContactTeacher.TeacherEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }
    fun getSpec(spec: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM teacherDB WHERE spec = '$spec'",null)
        return result.moveToFirst()
    }
    @SuppressLint("Range")
    fun getAllTeacherLogin(login: String):ArrayList<Teacher> {
        val teachers = ArrayList<Teacher>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor =
                db.rawQuery("select * from teacherDB where login = '${login}'",
                    null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.LOGIN)).toString()
                var fullname = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.FULLNAME)).toString()
                var password = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.PASSWORD)).toString()
                var email = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.EMAIL)).toString()
                var date = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.DATE)).toString()
                var spec = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.SPEC)).toString()
                teachers.add(Teacher(login, password, email, fullname, date, spec))
                cursor.moveToNext()
            }
        }
        return teachers
    }

    @SuppressLint("Range")
    fun getAllTeacherSpec(spec: String):ArrayList<Teacher> {
        val teachers = ArrayList<Teacher>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor =
                db.rawQuery("select * from teacherDB where spec = '${spec}'",
                    null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.LOGIN)).toString()
                var fullname = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.FULLNAME)).toString()
                var password = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.PASSWORD)).toString()
                var email = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.EMAIL)).toString()
                var date = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.DATE)).toString()
                var spec = cursor.getString(cursor.getColumnIndex(DBContactTeacher.TeacherEntry.SPEC)).toString()
                teachers.add(Teacher(login, password, email, fullname, date, spec))
                cursor.moveToNext()
            }
        }
        return teachers
    }
}