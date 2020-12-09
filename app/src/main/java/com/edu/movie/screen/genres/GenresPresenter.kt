package com.edu.movie.screen.genres

import com.edu.movie.data.model.Genres
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.data.source.repository.MovieRepository

class GenresPresenter(private val repository: MovieRepository) :
    ViewContactGenres.Presenter {

    private var view: ViewContactGenres.View? = null

    override fun onStart() {
        getGenres()
    }

    override fun onStop() {
        view = null
    }

    override fun setView(view: ViewContactGenres.View?) {
        this.view = view
    }

    override fun getGenres() {
        repository.getListGenres(object : OnFetchDataJsonListener<List<Genres>> {
            override fun onSuccess(data: List<Genres>) {
                view?.getGenresOnSuccess(data)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }
}
