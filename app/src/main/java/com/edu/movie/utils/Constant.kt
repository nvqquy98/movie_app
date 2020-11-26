package com.edu.movie.utils

import com.edu.movie.BuildConfig

object Constant {
    const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
    const val BASE_LANGUAGE = "&language=en-US"
    const val BASE_API_KEY = "?api_key=" + BuildConfig.API_KEY
}
