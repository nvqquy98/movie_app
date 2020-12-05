package com.edu.movie.utils

import com.edu.movie.BuildConfig

object Constant {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
    const val DEFAULT_PAGE = 1
    const val BASE_PAGE = "&page="
    const val BASE_LANGUAGE = "&language=en-US"
    const val BASE_API_KEY = "?api_key=" + BuildConfig.API_KEY
    const val BASE_DISCOVER = "discover/movie"
    const val SORT_BY_POPULAR = "&sort_by=popularity.desc"
    const val WITH_GENRES = "&with_genres="
    const val WITH_CAST = "&with_cast="
    const val WITH_COMPANY = "&with_companies="
    const val NULL_STRING = "null"
}
