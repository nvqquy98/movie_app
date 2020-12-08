package com.edu.movie.screen.favorite

import com.edu.movie.data.model.Favorite
import com.edu.movie.data.source.local.sqlite.ListenerDataFromDb
import com.edu.movie.data.source.repository.MovieRepository

class FavoritePresenter(private val repository: MovieRepository) : FavoriteContact.Presenter {

    private var view: FavoriteContact.View? = null

    override fun onStart() {}

    override fun onStop() {
        view = null
    }

    override fun setView(view: FavoriteContact.View?) {
        this.view = view
    }

    override fun getListFavorite() {
        repository.getListFavorite(object : ListenerDataFromDb<List<Favorite>> {
            override fun onSuccess(data: List<Favorite>?) {
                if (data != null) {
                    view?.loadListFavoriteSuccess(data)
                }
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }

    override fun deleteFavorite(id: Int) {
        repository.deleteFavorite(id, object : ListenerDataFromDb<Boolean> {
            override fun onSuccess(data: Boolean?) {
                view?.deleteSuccess(data == true)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }

}