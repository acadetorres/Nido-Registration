package com.acdetorres.nidoregistration

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

fun EditText.watchText (): MutableLiveData<String> {
    val str = MutableLiveData<String>()

    this.doOnTextChanged { text, start, before, count ->
        str.value = text.toString()

    }


    return str
}

fun EditText.clearText() {
    this.text.clear()
}

fun EditText.string() : String {
    return this.text.toString()
}

suspend fun Flow<AppState<Any>>.handleFlow() {

    this.collect {
        when (it) {
            AppState.Error -> TODO()
            is AppState.Progress -> TODO()
            is AppState.Success -> TODO()
        }
    }

}