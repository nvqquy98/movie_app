package com.edu.movie.data.source.remote

import com.edu.movie.utils.TypeModel
import com.edu.movie.data.source.MovieDataSource
import com.edu.movie.data.source.remote.fetchjson.GetJsonFromUrl

class MovieRemoteDataSource : MovieDataSource.Remote {

    private object Holder {
        val INSTANCE = MovieRemoteDataSource()
    }

    override fun <T> getData(
        listener: OnFetchDataListener<T>,
        stringUrl: String,
        typeMode: TypeModel
    ) {
        GetJsonFromUrl(listener, typeMode).execute(stringUrl)
    }

    companion object {
        val instance: MovieRemoteDataSource by lazy { Holder.INSTANCE }
    }
}
