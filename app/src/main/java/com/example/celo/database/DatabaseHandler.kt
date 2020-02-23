package com.example.celo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.celo.model.User

import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($TITLE TEXT, $FIRSTNAME TEXT, $LASTNAME TEXT, $GENDER TEXT, $DATEOFBIRTH TEXT, $EMAIL TEXT, $PHONE TEXT, $THUMBNAIL TEXT, $LARGEIMAGE TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addUser(userList: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE, userList.title)
        values.put(FIRSTNAME, userList.firstName)
        values.put(LASTNAME, userList.lastName)
        values.put(GENDER, userList.gender)
        values.put(DATEOFBIRTH, userList.dateOfBirth)
        values.put(EMAIL, userList.email)
        values.put(PHONE, userList.phone)
        values.put(THUMBNAIL, userList.thumbnail)
        values.put(LARGEIMAGE, userList.largeImage)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun addUsers(userList: List<User>): Boolean {
        val db = this.writableDatabase
        val _success = 0
        for(i in 0..userList.size-1) {
            val values = ContentValues()
            val user: User = userList.get(i)
            values.put(TITLE, user.title)
            values.put(FIRSTNAME, user.firstName)
            values.put(LASTNAME, user.lastName)
            values.put(GENDER, user.gender)
            values.put(DATEOFBIRTH, user.dateOfBirth)
            values.put(EMAIL, user.email)
            values.put(PHONE, user.phone)
            values.put(THUMBNAIL, user.thumbnail)
            values.put(LARGEIMAGE, user.largeImage)
            val _success = db.insert(TABLE_NAME, null, values)
        }
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun getUser(_id: Int): User {
        val user = User()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME "/*WHERE /$ID = $_id*/
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()

        user.title = cursor.getString(cursor.getColumnIndex(TITLE))
        user.firstName = cursor.getString(cursor.getColumnIndex(FIRSTNAME))
        user.lastName = cursor.getString(cursor.getColumnIndex(LASTNAME))
        user.gender = cursor.getString(cursor.getColumnIndex(GENDER))
        user.dateOfBirth = cursor.getString(cursor.getColumnIndex(DATEOFBIRTH))
        user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
        user.phone = cursor.getString(cursor.getColumnIndex(PHONE))
        user.thumbnail = cursor.getString(cursor.getColumnIndex(THUMBNAIL))
        user.largeImage = cursor.getString(cursor.getColumnIndex(LARGEIMAGE))
        cursor.close()
        return user
    }

    fun getUsers(): ArrayList<User> {
        val userList = ArrayList<User>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val user = User()
                    user.title = cursor.getString(cursor.getColumnIndex(TITLE))
                    user.firstName = cursor.getString(cursor.getColumnIndex(FIRSTNAME))
                    user.lastName = cursor.getString(cursor.getColumnIndex(LASTNAME))
                    user.gender = cursor.getString(cursor.getColumnIndex(GENDER))
                    user.dateOfBirth = cursor.getString(cursor.getColumnIndex(DATEOFBIRTH))
                    user.email = cursor.getString(cursor.getColumnIndex(EMAIL))
                    user.phone = cursor.getString(cursor.getColumnIndex(PHONE))
                    user.thumbnail = cursor.getString(cursor.getColumnIndex(THUMBNAIL))
                    user.largeImage = cursor.getString(cursor.getColumnIndex(LARGEIMAGE))
                    userList.add(user)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return userList
    }

    fun deleteAllUsers(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "UserDB"
        private val TABLE_NAME = "Users"

        private val TITLE = "title"
        private val FIRSTNAME = "firstName"
        private val LASTNAME = "lastName"
        private val GENDER = "gender"
        private val DATEOFBIRTH = "dateOfBirth"
        private val EMAIL = "email"
        private val PHONE = "phone"
        private var THUMBNAIL = "thumbnail"
        private var LARGEIMAGE = "largeImage"
    }
}