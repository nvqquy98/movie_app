package com.edu.movie.screen.favorite

import com.edu.movie.data.model.Favorite
import com.edu.movie.screen.base.BasePresenter

interface FavoriteContact {
    interface View {
        fun loadListFavoriteSuccess(listFavorite: List<Favorite>?)
        fun deleteSuccess(checkSuccess: Boolean?)
        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun getListFavorite()
        fun deleteFavorite(id: Int)
    }
}