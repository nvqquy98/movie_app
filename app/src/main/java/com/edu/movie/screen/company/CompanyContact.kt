package com.edu.movie.screen.company

import com.edu.movie.data.model.MovieItem
import com.edu.movie.screen.base.BasePresenter

interface CompanyContact {
    interface View {
        fun loadMoviesOnSuccess(movies: List<MovieItem>)
        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun getMovies(idCompany: Int, page: Int)
    }
}
