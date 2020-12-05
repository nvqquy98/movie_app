package com.edu.movie.screen.company

import com.edu.movie.data.model.MovieItem

interface CompanyContact {
    interface View {
        fun loadMoviesOnSuccess(movies: List<MovieItem>)
        fun onError(exception: Exception?)
    }
}