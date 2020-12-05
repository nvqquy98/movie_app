package com.edu.movie.screen.cast

import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.base.BasePresenter

class CastPresenter(private val repository: MovieRepository) : BasePresenter<CastContact.View> {
    private var view: CastContact.View? = null

    override fun onStart() {}

    override fun onStop() {
        view = null
    }

    override fun setView(view: CastContact.View?) {
        this.view = view
    }

    fun getMovies(idCast: Int, page: Int) {
        repository.getMoviesByCast(
            idCast,
            page,
            object : OnFetchDataJsonListener<List<MovieItem>> {
                override fun onSuccess(data: List<MovieItem>) {
                    view?.loadMoviesOnSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    view?.onError(exception)
                }
            })
    }
}