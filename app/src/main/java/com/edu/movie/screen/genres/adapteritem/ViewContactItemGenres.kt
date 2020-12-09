package com.edu.movie.screen.genres.adapteritem

import com.edu.movie.data.model.MovieItem
import com.edu.movie.screen.base.BasePresenter

interface ViewContactItemGenres {
    interface View : ViewContactItemGenres {
        fun getMoviesOnSuccess(movies: List<MovieItem>)

        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun getMoviesByIdGenre(id: Int)
    }
}
