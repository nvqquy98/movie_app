package com.edu.movie.data.source.remote

import com.edu.movie.data.source.MovieDataSource

class MovieRemoteDataSource : MovieDataSource.Remote {

    private object Holder {
        val INSTANCE = MovieRemoteDataSource()
    }

    companion object {
        val INSTANCE: MovieRemoteDataSource by lazy { Holder.INSTANCE }
    }
}