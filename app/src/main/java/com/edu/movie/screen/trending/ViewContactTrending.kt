package com.edu.movie.screen.trending

import com.edu.movie.data.model.MovieItem
import com.edu.movie.screen.base.BasePresenter
import com.edu.movie.utils.TrendingMoviesType

interface ViewContactTrending {
    interface View {
        fun getMovieSuccess(listMovies: MutableList<MovieItem>)
        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun getListMovie(
            page: Int,
            trendingMoviesType: TrendingMoviesType
        )
    }
}
