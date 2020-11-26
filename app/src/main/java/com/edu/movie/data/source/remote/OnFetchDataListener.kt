package com.edu.movie.data.source.remote

import com.edu.movie.utils.LoadError

interface OnFetchDataListener<T> : LoadError {
    fun onSuccess(list: T)
}
