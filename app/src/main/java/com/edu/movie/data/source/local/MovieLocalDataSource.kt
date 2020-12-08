package com.edu.movie.data.source.local

import android.content.ContentValues
import android.content.Context
import com.edu.movie.data.model.Favorite
import com.edu.movie.data.model.FavoriteTable
import com.edu.movie.data.model.TableName
import com.edu.movie.data.source.MovieDataSource
import com.edu.movie.data.source.local.sqlite.ListenerDataFromDb
import com.edu.movie.data.source.local.sqlite.SQLiteManager
import com.edu.movie.data.source.local.sqlite.SQLiteManagerImpl

class MovieLocalDataSource private constructor(private val context: Context?) :
    MovieDataSource.Local {

    private val sqLiteManager: SQLiteManager by lazy { SQLiteManagerImpl.newInstance(context) }

    override fun getListFavorite(listener: ListenerDataFromDb<List<Favorite>>) {
        try {
            val listFavorite = mutableListOf<Favorite>()
            sqLiteManager.getData(TableName.FAVORITE_TABLE)?.apply {
                if (moveToFirst())
                    do {
                        listFavorite.add(
                            Favorite(
                                getInt(FavoriteTable.ID.ordinal),
                                getString(FavoriteTable.TITLE.ordinal),
                                getDouble(FavoriteTable.RATE.ordinal),
                                getString(FavoriteTable.DESCRIPTION.ordinal),
                                getString(FavoriteTable.IMAGE.ordinal)
                            )
                        )
                    } while (moveToNext())
                close()
                listener.onSuccess(listFavorite)
            }
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    override fun getFavorite(id: Int, listener: ListenerDataFromDb<Favorite>) {
        try {
            var favorite: Favorite? = null
            sqLiteManager.getData(id, TableName.FAVORITE_TABLE, FavoriteTable.ID.column)?.apply {
                if (moveToFirst())
                    do {
                        favorite = Favorite(
                            getInt(FavoriteTable.ID.ordinal),
                            getString(FavoriteTable.TITLE.ordinal),
                            getDouble(FavoriteTable.RATE.ordinal),
                            getString(FavoriteTable.DESCRIPTION.ordinal),
                            getString(FavoriteTable.IMAGE.ordinal)
                        )

                    } while (moveToNext())
                close()
            }
            listener.onSuccess(favorite)
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    override fun addFavorite(favorite: Favorite, listener: ListenerDataFromDb<Boolean>) {
        try {
            val addSuccess = sqLiteManager.addData(ContentValues().apply {
                put(FavoriteTable.ID.column, favorite.id)
                put(FavoriteTable.TITLE.column, favorite.title)
                put(FavoriteTable.RATE.column, favorite.rate)
                put(FavoriteTable.DESCRIPTION.column, favorite.description)
                put(FavoriteTable.IMAGE.column, favorite.imagePath)
            }, TableName.FAVORITE_TABLE)
            listener.onSuccess(addSuccess)
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    override fun deleteData(id: Int, listener: ListenerDataFromDb<Boolean>) {
        try {
            val deleteSuccess =
                sqLiteManager.deleteData(id, TableName.FAVORITE_TABLE, FavoriteTable.ID.column)
            listener.onSuccess(deleteSuccess)
        } catch (e: Exception) {
            listener.onError(e)
        }
    }

    companion object {
        fun newInstance(context: Context?) = MovieLocalDataSource(context)
    }
}
