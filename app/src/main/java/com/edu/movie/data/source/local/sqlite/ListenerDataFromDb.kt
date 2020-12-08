package com.edu.movie.data.source.local.sqlite

interface ListenerDataFromDb<T> {
    fun onSuccess(data: T?)
    fun onError(exception: Exception?)

}