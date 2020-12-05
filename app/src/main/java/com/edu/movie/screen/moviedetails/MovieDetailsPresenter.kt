package com.edu.movie.screen.moviedetails

import com.edu.movie.data.model.*
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.data.source.repository.MovieRepository

class MovieDetailsPresenter(private val repository: MovieRepository) :
    MovieDetailsContact.Presenter {
    private var view: MovieDetailsContact.View? = null

    override fun onStart() {}

    override fun onStop() {
        view = null
    }

    override fun setView(view: MovieDetailsContact.View?) {
        this.view = view
    }

    override fun getMovieDetails(id: Int) {
        repository.getMovieDetails(id, object : OnFetchDataJsonListener<MovieDetails> {
            override fun onSuccess(data: MovieDetails) {
                view?.loadContentMovieOnSuccess(data)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }
        })
    }

    override fun getVideoTrailer(idMovieDetails: Int) {
        repository.getListVideosInMovieDetails(
            idMovieDetails,
            object : OnFetchDataJsonListener<List<VideoYoutube>> {
                override fun onSuccess(data: List<VideoYoutube>) {
                    view?.loadVideoTrailerOnSuccess(data.firstOrNull { it.type == VideoYoutubeEntry.TYPE_TRAILER })
                }

                override fun onError(exception: Exception?) {
                    view?.onError(exception)
                }
            })
    }

    override fun getCastsInMovieDetails(idMovieDetails: Int) {
        repository.getListCastsInMovieDetails(
            idMovieDetails,
            object : OnFetchDataJsonListener<List<Cast>> {
                override fun onSuccess(data: List<Cast>) {
                    view?.loadCastsOnSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    view?.onError(exception)
                }
            })
    }

    override fun getListMovieRecommendations(idMovieDetails: Int) {
        repository.getListMovieRecommendations(
            idMovieDetails,
            object : OnFetchDataJsonListener<List<MovieItem>> {
                override fun onSuccess(data: List<MovieItem>) {
                    view?.loadMoviesRecommendations(data)
                }

                override fun onError(exception: Exception?) {
                    view?.onError(exception)
                }
            })
    }
}
