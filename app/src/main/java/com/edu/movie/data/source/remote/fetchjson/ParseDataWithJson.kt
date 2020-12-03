package com.edu.movie.data.source.remote.fetchjson

import com.edu.movie.data.model.CastEntry
import com.edu.movie.data.model.ItemMovieSliderEntry
import com.edu.movie.data.model.MovieEntry
import com.edu.movie.data.model.VideoYoutubeEntry
import com.edu.movie.utils.TypeModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ParseDataWithJson {

    @Throws(Exception::class)
    fun getJsonFromUrl(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnect = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = METHOD_GET
            connectTimeout = TIME_OUT
            readTimeout = TIME_OUT
            doOutput = true
            connect()
        }
        val bufferedReader = BufferedReader(InputStreamReader(url.openStream()))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        httpURLConnect.disconnect()
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
                TypeModel.MOVIE_ITEM_SLIDER -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(ItemMovieSliderEntry.LIST_MOVIE),
                        typeModel
                    )
                }
                TypeModel.MOVIE_DETAILS -> {
                    parseJsonToObject(
                        JSONObject(jsonString),
                        typeModel
                    )
                }
                TypeModel.VIDEO_YOUTUBE -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(VideoYoutubeEntry.LIST_VIDEOS),
                        typeModel
                    )

                }
                TypeModel.CAST -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(CastEntry.LIST_CASTS),
                        typeModel
                    )
                }
                TypeModel.COMPANY -> {
                    parseJsonToList(JSONArray(jsonString), typeModel)
                }
                TypeModel.GENRES -> {
                    parseJsonToList(JSONArray(jsonString), typeModel)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    @Throws(Exception::class)
    private fun parseJsonToObject(jsonObject: JSONObject?, typeModel: TypeModel): Any? {
        val parseJsonToModel = ParseJsonToModel()
        when (typeModel) {
            TypeModel.MOVIE_ITEM_TRENDING -> {
                return parseJsonToModel.parseJsonToMovieItem(jsonObject)
            }
            TypeModel.MOVIE_ITEM_SLIDER -> {
                return parseJsonToModel.parseJsonToMovieItemSlider(jsonObject)
            }
            TypeModel.MOVIE_DETAILS -> {
                return parseJsonToModel.parseJsonToMovieDetails(jsonObject)
            }
            TypeModel.VIDEO_YOUTUBE -> {
                return parseJsonToModel.parseJsonToVideosYoutube(jsonObject)
            }
            TypeModel.CAST -> {
                return parseJsonToModel.parseJsonToCast(jsonObject)
            }
            TypeModel.GENRES -> {
                return parseJsonToModel.parseJsonToGenres(jsonObject)
            }
            TypeModel.COMPANY -> {
                return parseJsonToModel.parseJsonToCompany(jsonObject)
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
    }
}
