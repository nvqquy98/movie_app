package com.edu.movie.data.source.remote.fetchjson

import com.edu.movie.data.model.MovieEntry
import com.edu.movie.data.model.MovieItem
import org.json.JSONObject

class ParseJsonToModel private constructor() {

    private object Holder {
        val INSTANCE = ParseJsonToModel()
    }

    @Throws(Exception::class)
    fun parseJsonToMovieItem(jsonObject: JSONObject?): MovieItem? {
        jsonObject?.apply {
            return MovieItem(
                getInt(MovieEntry.ID),
                getString(MovieEntry.TITLE),
                getString(MovieEntry.IMAGE_URL),
                getInt(MovieEntry.RATE)
            )
        }
        return null
    }

    companion object {
        val instance: ParseJsonToModel by lazy { Holder.INSTANCE }
    }
}
