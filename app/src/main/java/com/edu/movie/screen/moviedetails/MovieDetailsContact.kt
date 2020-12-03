package com.edu.movie.screen.moviedetails

import com.edu.movie.data.model.Cast
import com.edu.movie.data.model.MovieDetails
import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.model.VideoYoutube

interface MovieDetailsContact {
    interface View {
        fun getMovieDetailsSuccess(movieDetails: MovieDetails)
        fun getListCastsSuccess(casts: List<Cast>)
        fun getVideosTrailerSuccess(video: VideoYoutube?)
        fun getListMovieRecommendations(movies: List<MovieItem>)
        fun onError(exception: Exception?)
    }
}
