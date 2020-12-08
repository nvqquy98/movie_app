package com.edu.movie.data.source

import com.edu.movie.data.model.Favorite
import com.edu.movie.data.source.local.sqlite.ListenerDataFromDb
import com.edu.movie.data.source.remote.OnFetchDataJsonListener
import com.edu.movie.utils.TrendingMoviesType
import com.edu.movie.utils.TypeEndPointMovieDetails

interface MovieDataSource {
    /**
     *  Local
     */
    interface Local {
        fun getListFavorite(listener: ListenerDataFromDb<List<Favorite>>)
        fun getFavorite(id: Int, listener: ListenerDataFromDb<Favorite>)
        fun addFavorite(favorite: Favorite, listener: ListenerDataFromDb<Boolean>)
        fun deleteData(id: Int, listener: ListenerDataFromDb<Boolean>)
    }

    /**
     *  Remote
     */
    interface Remote {
        fun <T> getDataTrending(
            page: Int,
            trendingType: TrendingMoviesType,
            listener: OnFetchDataJsonListener<T>
        )

        fun <T> getDataSlider(listener: OnFetchDataJsonListener<T>)

        fun <T> getDataInMovieDetails(
            idMovie: Int,
            typeEndPoint: TypeEndPointMovieDetails,
            listener: OnFetchDataJsonListener<T>
        )

        fun <T> getDataGenres(listener: OnFetchDataJsonListener<T>)

        fun <T> getMoviesByIdGenre(idGenre: Int, page: Int, listener: OnFetchDataJsonListener<T>)

        fun <T> getMoviesByCast(idCast: Int, page: Int, listener: OnFetchDataJsonListener<T>)

        fun <T> getMoviesByCompany(idCompany: Int, page: Int, listener: OnFetchDataJsonListener<T>)
    }
}
