package com.edu.movie.screen.moviedetails

import com.edu.movie.data.model.*
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.data.source.repository.MovieRepository
import com.edu.movie.screen.base.BasePresenter

class MovieDetailsPresenter(private val repository: MovieRepository) :
    BasePresenter<MovieDetailsContact.View> {
    private var view: MovieDetailsContact.View? = null

    override fun onStart() {}

    override fun onStop() {}

    override fun setView(view: MovieDetailsContact.View?) {
        this.view = view
    }

    fun getMovieDetails(id: Int) {
        repository.getMovieDetails(id, object : OnFetchDataJsonListener<MovieDetails> {
            override fun onSuccess(data: MovieDetails) {
                view?.getMovieDetailsSuccess(data)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }

    fun getVideoTrailer(idMovieDetails: Int) {
        repository.getListVideosInMovieDetails(idMovieDetails, object : OnFetchDataJsonListener<List<VideoYoutube>> {
            override fun onSuccess(data: List<VideoYoutube>) {
                view?.getVideosTrailerSuccess(data.firstOrNull { it.type == VideoYoutubeEntry.TYPE_TRAILER })
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }

    fun getCastsInMovieDetails(idMovieDetails: Int) {
        repository.getListCastsInMovieDetails(idMovieDetails, object : OnFetchDataJsonListener<List<Cast>> {
            override fun onSuccess(data: List<Cast>) {
                view?.getListCastsSuccess(data)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }

    fun getListMovieRecommendations(idMovieDetails: Int) {
        repository.getListMovieRecommendations(idMovieDetails, object : OnFetchDataJsonListener<List<MovieItem>> {
            override fun onSuccess(data: List<MovieItem>) {
                view?.getListMovieRecommendations(data)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }
}