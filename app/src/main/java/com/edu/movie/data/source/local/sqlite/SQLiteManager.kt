package com.edu.movie.data.source.local.sqlite

import android.content.ContentValues
import android.database.Cursor

interface SQLiteManager {
    fun getData(tableName: String): Cursor?
    fun <T> getData(id: T, tableName: String, columnName: String): Cursor?
    fun addData(data: ContentValues, tableName: String): Boolean
    fun <T> deleteData(id: T, tableName: String, columnName: String): Boolean
}