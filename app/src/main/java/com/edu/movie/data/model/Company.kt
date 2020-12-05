package com.edu.movie.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(val id: Int?, val name: String?, val logoUrl: String?) : Parcelable

object CompanyEntry {
    const val ID = "id"
    const val NAME = "name"
    const val LOGO_URL = "logo_path"
}
