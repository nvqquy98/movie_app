package com.edu.movie.data.model

data class MovieItem(
    val id: Int?,
    val title: String?,
    val imageUrl: String?,
    val rate: Int?
)

object MovieEntry {
    const val LIST_MOVIE = "results"
    const val ID = "id"
    const val IMAGE_URL = "poster_path"
    const val TITLE = "title"
    const val RATE = "vote_average"
}
