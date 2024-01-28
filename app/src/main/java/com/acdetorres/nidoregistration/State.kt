package com.acdetorres.nidoregistration

import androidx.lifecycle.MutableLiveData

sealed class AppState<out T> {
    class Progress(val isLoading: Boolean) : AppState<Nothing>()
    class Success<out R>(val data: R?): AppState<R>()
    data object Error : AppState<Nothing>()
}

