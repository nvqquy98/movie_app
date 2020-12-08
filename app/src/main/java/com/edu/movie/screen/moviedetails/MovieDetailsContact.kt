package com.edu.movie.screen.moviedetails

import com.edu.movie.data.model.*
import com.edu.movie.screen.base.BasePresenter

interface MovieDetailsContact {
    interface View {
        /**
         * Remote
         */
        fun loadContentMovieOnSuccess(movieDetails: MovieDetails)
        fun loadCastsOnSuccess(casts: List<Cast>)
        fun loadVideoTrailerOnSuccess(video: VideoYoutube?)
        fun loadMoviesRecommendations(movies: List<MovieItem>)
        fun onError(exception: Exception?)

        /**
         * Local
         */
        fun getFavorite(favorite: Favorite?)
        fun addFavoriteSuccess(isSuccess: Boolean)
        fun unFavoriteSuccess(isSuccess: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        /**
         * Remote
         */
        fun getMovieDetails(id: Int)
        fun getVideoTrailer(idMovieDetails: Int)
        fun getCastsInMovieDetails(idMovieDetails: Int)
        fun getListMovieRecommendations(idMovieDetails: Int)

        /**
         * Local
         */
        fun getFavorite(id: Int)
        fun addFavorite(favorite: Favorite)
        fun deleteFavorite(id: Int)
    }
}
