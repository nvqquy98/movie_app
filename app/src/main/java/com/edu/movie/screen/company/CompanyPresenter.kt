package com.edu.movie.screen.company

import com.edu.movie.data.model.MovieItem
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.base.BasePresenter

class CompanyPresenter(private val repository: MovieRepository) :
    BasePresenter<CompanyContact.View> {
    private var view: CompanyContact.View? = null

    override fun onStart() {}

    override fun onStop() {
        view = null
    }

    override fun setView(view: CompanyContact.View?) {
        this.view = view
    }

    fun getMovies(idCompany: Int, page: Int) {
        repository.getMoviesByCompany(
            idCompany,
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