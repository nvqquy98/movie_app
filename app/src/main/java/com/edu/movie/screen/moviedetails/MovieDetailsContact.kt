package com.edu.movie.screen.moviedetails

import com.edu.movie.data.model.Cast
import com.edu.movie.data.model.MovieDetails
import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.model.VideoYoutube
import com.edu.movie.screen.base.BasePresenter

interface MovieDetailsContact {
    interface View {
        fun loadContentMovieOnSuccess(movieDetails: MovieDetails)
        fun loadCastsOnSuccess(casts: List<Cast>)
        fun loadVideoTrailerOnSuccess(video: VideoYoutube?)
        fun loadMoviesRecommendations(movies: List<MovieItem>)
        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun getMovieDetails(id: Int)
        fun getVideoTrailer(idMovieDetails: Int)
        fun getListMovieRecommendations(idMovieDetails: Int)
        fun getCastsInMovieDetails(idMovieDetails: Int)
    }
}
