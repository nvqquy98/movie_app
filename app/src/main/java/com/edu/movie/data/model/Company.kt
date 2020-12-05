package com.edu.movie.data.model

import java.io.Serializable

data class Company(val id: Int?, val name: String?, val logoUrl: String?) : Serializable

object CompanyEntry {
    const val ID = "id"
    const val NAME = "name"
    const val LOGO_URL = "logo_path"
}
