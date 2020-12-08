package com.edu.movie.data.source.local.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.edu.movie.data.model.FavoriteTable
import com.edu.movie.data.model.TableName
import com.edu.movie.utils.Constant

class SQLiteManagerImpl private constructor(context: Context?) :
    SQLiteOpenHelper(context, ConstantSQL.DATABASE_NAME, null, ConstantSQL.VERSION), SQLiteManager {


    override fun onCreate(db: SQLiteDatabase?) {
        val stringCreateTableFavorite =
            "CREATE TABLE " + TableName.FAVORITE_TABLE + " ( " +
                    FavoriteTable.ID.column + " INTEGER PRIMARY KEY, " +
                    FavoriteTable.TITLE.column + " TEXT, " +
                    FavoriteTable.RATE.column + " REAL," +
                    FavoriteTable.DESCRIPTION.column + " TEXT, " +
                    FavoriteTable.IMAGE.column + " TEXT " + " ) "

        db?.execSQL(stringCreateTableFavorite)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    @Throws(Exception::class)
    override fun getData(tableName: String): Cursor? {
        val stringQueryAll = "SELECT * FROM $tableName"
        return readableDatabase.rawQuery(stringQueryAll, null)
    }


    @Throws(Exception::class)
    override fun <T> getData(id: T, tableName: String, columnName: String): Cursor? {
        val stringQuery = "SELECT * FROM $tableName WHERE $columnName = $id"
        return readableDatabase.rawQuery(stringQuery, null)
    }

    @Throws(Exception::class)
    override fun addData(data: ContentValues, tableName: String): Boolean =
        writableDatabase.insert(tableName, null, data) > Constant.NUMBER_0

    @Throws(Exception::class)
    override fun <T> deleteData(id: T, tableName: String, columnName: String): Boolean =
        writableDatabase.delete(
            tableName,
            columnName + PARAM_QUERY_DEFAULT,
            arrayOf(id.toString())
        ) > Constant.NUMBER_0

    companion object {
        @Volatile
        private var INSTANCE: SQLiteManagerImpl? = null
        private const val PARAM_QUERY_DEFAULT = " =? "

        private fun buildDatabase(context: Context?): SQLiteManagerImpl =
            SQLiteManagerImpl(context?.applicationContext)

        fun newInstance(context: Context?): SQLiteManagerImpl = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
    }
}