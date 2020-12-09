package com.edu.movie.screen.genres

import com.edu.movie.data.model.Genres
import com.edu.movie.screen.base.BasePresenter

interface ViewContactGenres {
    interface View : ViewContactGenres {
        fun getGenresOnSuccess(genres: List<Genres>)

        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun getGenres()
    }
}
