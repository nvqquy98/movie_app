package com.edu.movie.data.source

import com.edu.movie.utils.TypeModel
import com.edu.movie.data.source.remote.OnFetchDataListener

interface MovieDataSource {
    /**
     *  Local
     */
    interface Local

    /**
     *  Remote
     */
    interface Remote {
        fun <T> getData(listener: OnFetchDataListener<T>, stringUrl: String, typeModel: TypeModel)
    }
}
