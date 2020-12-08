package com.edu.movie.data.model

data class Favorite(
    val id: Int,
    val title: String,
    val rate: Double?,
    val description: String?,
    val imagePath: String?
)

enum class FavoriteTable(val column: String) {
    ID("Id"),
    TITLE("Title"),
    RATE("Rate"),
    DESCRIPTION("Description"),
    IMAGE("ImagePath")
}