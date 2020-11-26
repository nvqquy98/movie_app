package com.edu.movie.data.source

import com.edu.movie.data.model.MovieItem
import com.edu.movie.utils.TypeModel
import com.edu.movie.data.source.local.MovieLocalDataSource
import com.edu.movie.data.source.remote.MovieRemoteDataSource
import com.edu.movie.data.source.remote.OnFetchDataListener

class MovieRepository private constructor(
    private val local: MovieDataSource.Local,
    private val remote: MovieDataSource.Remote
) {

    private object Holder {
        val INSTANCE = MovieRepository(
            local = MovieLocalDataSource.instance,
            remote = MovieRemoteDataSource.instance
        )
    }

    fun getListMovieTrending(
        listener: OnFetchDataListener<MutableList<MovieItem>>,
        stringUrl: String,
        typeModel: TypeModel
    ) {
        remote.getData(listener, stringUrl, typeModel)
    }

    companion object {
        val instance: MovieRepository by lazy { Holder.INSTANCE }
    }
}
