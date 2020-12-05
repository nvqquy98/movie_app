package com.edu.movie.screen.cast

import com.edu.movie.data.model.MovieItem

interface CastContact {
    interface View {
        fun loadMoviesOnSuccess(movies: List<MovieItem>)
        fun onError(exception: Exception?)
    }
}