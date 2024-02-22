package com.example.zd5.DBSpecialty

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zd5.DBUniversity.DBContactUniversity

class DBHelperSpecialty (val context: Context): SQLiteOpenHelper(context,
    DBContactSpecialty.SpecialtyEntry.TABLE_NAME,  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val quary =
            "CREATE TABLE specialtyDB (id INT PRIMARY KEY, title TEXT, type TEXT, load TEXT)"
        db!!.execSQL(quary)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS specialtyDB")
        onCreate(db)
    }

    fun addSpecialty(specialty: Specialty)
    {
        val values = ContentValues()
        values.put("title", specialty.title)
        values.put("type", specialty.type)
        values.put("load", specialty.load)
        val db = this.writableDatabase
        db.insert("specialtyDB", null, values)
        db.close()
    }

    fun deleteAll(): Boolean {
        val db = writableDatabase
        db.delete("specialtyDB", null, null)
        return true
    }
    @Throws(SQLiteConstraintException::class)
    fun deleteSpec(title: String): Boolean {
        val db = writableDatabase
        val selection = DBContactSpecialty.SpecialtyEntry.TITLE + " LIKE ?"
        val selectionArgs = arrayOf(title)
        db.delete(DBContactSpecialty.SpecialtyEntry.TABLE_NAME, selection, selectionArgs)
        return true
    }


    fun getSpec(title: String):Boolean
    {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM specialtyDB WHERE title = '$title'",null)
        return result.moveToFirst()
    }

    @SuppressLint("Range")
    fun readAllSpec(): ArrayList<Specialty> {
        val spec = ArrayList<Specialty>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from specialtyDB", null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var title = cursor.getString(cursor.getColumnIndex(DBContactSpecialty.SpecialtyEntry.TITLE)).toString()
                var type = cursor.getString(cursor.getColumnIndex(DBContactSpecialty.SpecialtyEntry.TYPE)).toString()
                var load = cursor.getString(cursor.getColumnIndex(DBContactSpecialty.SpecialtyEntry.LOAD)).toString()
                spec.add(Specialty(title, type, load))
                cursor.moveToNext()
            }
        }
        return spec
    }

    @SuppressLint("Range")
    fun readAllSpecTitle(title: String): ArrayList<Specialty> {
        val spec = ArrayList<Specialty>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from specialtyDB where title = '$title'", null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                var title = cursor.getString(cursor.getColumnIndex(DBContactSpecialty.SpecialtyEntry.TITLE)).toString()
                var type = cursor.getString(cursor.getColumnIndex(DBContactSpecialty.SpecialtyEntry.TYPE)).toString()
                var load = cursor.getString(cursor.getColumnIndex(DBContactSpecialty.SpecialtyEntry.LOAD)).toString()
                spec.add(Specialty(title, type, load))
                cursor.moveToNext()
            }
        }
        return spec
    }
}