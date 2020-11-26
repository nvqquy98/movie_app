package com.edu.movie.data.source.remote.fetchjson

import android.os.AsyncTask
import com.edu.movie.utils.TypeModel
import com.edu.movie.data.source.remote.OnFetchDataListener

class GetJsonFromUrl<T> constructor(
    private val listener: OnFetchDataListener<T>,
    private val typeMode: TypeModel
) : AsyncTask<String?, Unit?, String>() {

    private var exception: Exception? = null
    private val parseDataWithJson: ParseDataWithJson by lazy { ParseDataWithJson.instance }
    override fun doInBackground(vararg params: String?): String {
        var data = ""
        try {
            params[0]?.let { data = parseDataWithJson.getJsonFromUrl(it) }
        } catch (e: Exception) {
            exception = e
        }
        return data
    }

    override fun onPostExecute(result: String) {
        if (result == "") listener.loadError(exception)
        @Suppress("UNCHECKED_CAST")
        val data = parseDataWithJson.parseJson(result, typeMode)
        data?.let {
            listener.onSuccess(it as T)
        }
    }
}
