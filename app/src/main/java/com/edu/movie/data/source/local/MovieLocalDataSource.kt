package com.edu.movie.data.source.local

import com.edu.movie.data.source.MovieDataSource


class MovieLocalDataSource : MovieDataSource.Local {

    private object Holder {
        val INSTANCE = MovieLocalDataSource()
    }

    companion object {
        val INSTANCE: MovieLocalDataSource by lazy { Holder.INSTANCE }
    }
}