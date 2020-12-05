package com.edu.movie.data.model

import java.io.Serializable

data class Cast(val id: Int?, val name: String?, val imageUrl: String?) : Serializable

object CastEntry {
    const val LIST_CASTS = "cast"
    const val ID = "id"
    const val NAME = "name"
    const val IMAGE_URL = "profile_path"
}
