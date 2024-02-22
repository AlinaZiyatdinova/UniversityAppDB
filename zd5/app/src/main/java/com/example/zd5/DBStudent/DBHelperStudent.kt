package com.example.zd5.DBStudent

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelperStudent (val context: Context):
    SQLiteOpenHelper (context,DBContactStudent.StudentEntry.TABLE_NAME,  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val quary = "CREATE TABLE studentDB (id INT PRIMARY KEY, login TEXT, password TEXT, email TEXT, fullname TEXT, date TEXT, point TEXT, spec TEXT)"
        db!!.execSQL(quary)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS studentDB")
        onCreate(db)
    }

    fun getStudent(login: String, email: String, spec: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM studentDB WHERE login = '$login' AND spec ='$spec'",null)
        return result.moveToFirst()
    }

    fun getStudent(login: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM studentDB WHERE login = '$login'",null)
        return result.moveToFirst()
    }

    fun getSpec(spec: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM studentDB WHERE spec = '$spec'",null)
        return result.moveToFirst()
    }

    fun getDBSize():Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM studentDB",null)
        return result.moveToFirst()
    }

    fun addStudent(student: Student)
    {
        val values = ContentValues()
        values.put("login", student.login)
        values.put("password", student.password)
        values.put("email", student.email)
        values.put("fullname", student.fullname)
        values.put("date", student.date)
        values.put("point", student.point)
        values.put("spec", student.spec)

        val db = this.writableDatabase
        db.insert("studentDB", null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun readAllStudents(spec: String): ArrayList<Student> {
        val student = ArrayList<Student>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from studentDB where spec = '$spec'", null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.LOGIN)).toString()
                var password = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.PASSWORD)).toString()
                var email = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.EMAIL)).toString()
                var fullname = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.FULLNAME)).toString()
                var date = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.DATE)).toString()
                var point = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.POINT)).toString()
                var spec = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.SPEC)).toString()
                student.add(Student(login, password, email, fullname, date, point, spec))
                cursor.moveToNext()
            }
        }
        return student
    }

    fun deleteAllStudents(): Boolean {
        val db = writableDatabase
        db.delete("studentDB", null, null)
        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteStudent(login: String, spec: String): Boolean {
        val db = writableDatabase
        val selection = "${DBContactStudent.StudentEntry.LOGIN} = ? AND ${DBContactStudent.StudentEntry.SPEC} = ?"
        val selectionArgs = arrayOf(login, spec)
        db.delete(DBContactStudent.StudentEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }

    @SuppressLint("Range")
    fun getInfoStudent(fullname: String):ArrayList<Student> {
        val student = ArrayList<Student>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor =
                db.rawQuery("select * from studentDB where fullname = '$fullname'",
                    null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.LOGIN)).toString()
                var password = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.PASSWORD)).toString()
                var email = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.EMAIL)).toString()
                var fullname = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.FULLNAME)).toString()
                var date = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.DATE)).toString()
                var point = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.POINT)).toString()
                var spec = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.SPEC)).toString()
                student.add(Student(login, password, email, fullname, date, point, spec))
                cursor.moveToNext()
            }
        }
        return student
    }

    @SuppressLint("Range")
    fun getStudentLogin(login: String):ArrayList<Student> {
        val student = ArrayList<Student>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor =
                db.rawQuery("select * from studentDB where login = '$login'",
                    null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.LOGIN)).toString()
                var password = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.PASSWORD)).toString()
                var email = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.EMAIL)).toString()
                var fullname = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.FULLNAME)).toString()
                var date = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.DATE)).toString()
                var point = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.POINT)).toString()
                var spec = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.SPEC)).toString()
                student.add(Student(login, password, email, fullname, date, point, spec))
                cursor.moveToNext()
            }
        }
        return student
    }

    @SuppressLint("Range")
    fun getStudentSpec( spec: String):ArrayList<Student> {
        val student = ArrayList<Student>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor =
                db.rawQuery("select * from studentDB where spec = '$spec'",
                    null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var login = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.LOGIN)).toString()
                var password = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.PASSWORD)).toString()
                var email = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.EMAIL)).toString()
                var fullname = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.FULLNAME)).toString()
                var date = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.DATE)).toString()
                var point = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.POINT)).toString()
                var spec = cursor.getString(cursor.getColumnIndex(DBContactStudent.StudentEntry.SPEC)).toString()
                student.add(Student(login, password, email, fullname, date, point, spec))
                cursor.moveToNext()
            }
        }
        return student
    }

}