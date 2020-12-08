package com.edu.movie.data.source.repository

import android.content.Context
import com.edu.movie.data.model.*
import com.edu.movie.data.source.MovieDataSource
import com.edu.movie.data.source.local.MovieLocalDataSource
import com.edu.movie.data.source.local.sqlite.ListenerDataFromDb
import com.edu.movie.data.source.remote.MovieRemoteDataSource
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.utils.TrendingMoviesType
import com.edu.movie.utils.TypeEndPointMovieDetails

class MovieRepository private constructor(
    private val local: MovieDataSource.Local,
    private val remote: MovieDataSource.Remote
) {

    private object Holder {
        val INSTANCE = MovieRepository(
            local = MovieLocalDataSource.newInstance(null),
            remote = MovieRemoteDataSource.instance
        )
    }

    fun getListMovieTrending(
        page: Int,
        trendingType: TrendingMoviesType,
        listener: OnFetchDataJsonListener<MutableList<MovieItem>>
    ) {
        remote.getDataTrending(page, trendingType, listener)
    }

    fun getListMovieSlider(listener: OnFetchDataJsonListener<MutableList<ItemMovieSlider>>) {
        remote.getDataSlider(listener)
    }

    fun getMovieDetails(id: Int, listener: OnFetchDataJsonListener<MovieDetails>) {
        remote.getDataInMovieDetails(id, TypeEndPointMovieDetails.MOVIE_DETAILS, listener)
    }

    fun getListCastsInMovieDetails(
        idMovieDetails: Int,
        listener: OnFetchDataJsonListener<List<Cast>>
    ) {
        remote.getDataInMovieDetails(idMovieDetails, TypeEndPointMovieDetails.CASTS, listener)
    }

    fun getListVideosInMovieDetails(
        idMovieDetails: Int,
        listener: OnFetchDataJsonListener<List<VideoYoutube>>
    ) {
        remote.getDataInMovieDetails(
            idMovieDetails,
            TypeEndPointMovieDetails.VIDEO_YOUTUBE,
            listener
        )
    }

    fun getListMovieRecommendations(
        idMovieDetails: Int,
        listener: OnFetchDataJsonListener<List<MovieItem>>
    ) {
        remote.getDataInMovieDetails(
            idMovieDetails,
            TypeEndPointMovieDetails.RECOMMENDATIONS,
            listener
        )
    }

    fun getListGenres(listener: OnFetchDataJsonListener<List<Genres>>) {
        remote.getDataGenres(listener)
    }

    fun getMoviesByIdGenre(id: Int, page: Int, listener: OnFetchDataJsonListener<List<MovieItem>>) {
        remote.getMoviesByIdGenre(id, page, listener)
    }

    fun getMoviesByCast(
        idCast: Int,
        page: Int,
        listener: OnFetchDataJsonListener<List<MovieItem>>
    ) {
        remote.getMoviesByCast(idCast, page, listener)
    }

    fun getMoviesByCompany(
        idCompany: Int,
        page: Int,
        listener: OnFetchDataJsonListener<List<MovieItem>>
    ) {
        remote.getMoviesByCompany(idCompany, page, listener)
    }

    fun getListFavorite(listener: ListenerDataFromDb<List<Favorite>>) {
        local.getListFavorite(listener)
    }


    fun getFavorite(id: Int, listener: ListenerDataFromDb<Favorite>) {
        local.getFavorite(id, listener)
    }

    fun addFavorite(favorite: Favorite, listener: ListenerDataFromDb<Boolean>) {
        local.addFavorite(favorite, listener)
    }

    fun deleteFavorite(id: Int, listener: ListenerDataFromDb<Boolean>) {
        local.deleteData(id, listener)
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }
        fun newInstance(context: Context?) =
            MovieRepository(
                MovieLocalDataSource.newInstance(context),
                MovieRemoteDataSource.instance
            )
    }
}
