package com.edu.movie.data.source.remote.fetchjson

import com.edu.movie.data.model.MovieEntry
import com.edu.movie.utils.TypeModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ParseDataWithJson private constructor() {

    private object Holder {
        val INSTANCE = ParseDataWithJson()
    }

    @Throws(Exception::class)
    fun getJsonFromUrl(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnect = url.openConnection() as HttpURLConnection
        httpURLConnect.requestMethod = METHOD_GET
        httpURLConnect.connectTimeout = TIME_OUT
        httpURLConnect.readTimeout = TIME_OUT
        httpURLConnect.doOutput = true
        httpURLConnect.connect()

        val bufferedReader = BufferedReader(InputStreamReader(url.openStream()))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        return stringBuilder.toString()
    }

    fun parseJson(jsonString: String, typeModel: TypeModel): Any? =
        try {
            when (typeModel) {
                TypeModel.MOVIE_ITEM_TRENDING -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(MovieEntry.LIST_MOVIE),
                        typeModel
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    @Throws(Exception::class)
    private fun parseJsonToObject(jsonObject: JSONObject?, typeModel: TypeModel): Any? {
        val parseJsonToModel = ParseJsonToModel.instance
        when (typeModel) {
            TypeModel.MOVIE_ITEM_TRENDING -> {
                return parseJsonToModel.parseJsonToMovieItem(jsonObject)
            }
        }
    }

    @Throws(Exception::class)
    private fun parseJsonToList(jsonArray: JSONArray?, typeModel: TypeModel): Any {
        val data = mutableListOf<Any?>()
        for (i in 0 until (jsonArray?.length() ?: 0)) {
            val jsonObject = jsonArray?.getJSONObject(i)
            data.add(parseJsonToObject(jsonObject, typeModel))
        }
        return data.filterNotNull()
    }

    companion object {
        private const val TIME_OUT = 20000
        private const val METHOD_GET = "GET"
        val instance: ParseDataWithJson by lazy { Holder.INSTANCE }
    }
}
