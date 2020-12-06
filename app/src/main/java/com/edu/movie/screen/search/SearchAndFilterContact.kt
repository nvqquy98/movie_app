package com.edu.movie.screen.search

import com.edu.movie.data.model.Genres
import com.edu.movie.data.model.MovieItem

interface ViewContact {
    interface SearchView {
        fun onListenerAcceptFilter(rateValue: Double, genre: Genres?)
        fun loadMoviesOnSuccess(movies: List<MovieItem>)
        fun onError(exception: Exception?)
    }

    interface FilterView {
        fun setView(view: SearchView)
        fun loadGenresOnSuccess(genres: List<Genres>)
        fun onError(exception: Exception?)
    }
}
