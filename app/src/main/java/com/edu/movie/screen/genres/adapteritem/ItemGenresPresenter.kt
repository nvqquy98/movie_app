package com.edu.movie.screen.genres.adapteritem

import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.utils.Constant

class ItemGenresPresenter(private val repository: MovieRepository) :
    ViewContactItemGenres.Presenter {

    private var view: ViewContactItemGenres.View? = null

    override fun onStart() {}

    override fun onStop() {
        view = null
    }

    override fun setView(view: ViewContactItemGenres.View?) {
        this.view = view
    }

    override fun getMoviesByIdGenre(id: Int) {
        repository.getMoviesByIdGenre(
            id,
            Constant.DEFAULT_PAGE,
            object : OnFetchDataJsonListener<List<MovieItem>> {
                override fun onSuccess(data: List<MovieItem>) {
                    view?.getMoviesOnSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    view?.onError(exception)
                }

            })
    }
}
