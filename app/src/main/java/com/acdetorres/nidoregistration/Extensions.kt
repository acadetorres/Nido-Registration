package com.acdetorres.nidoregistration

import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import retrofit2.HttpException
import java.net.UnknownHostException

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


fun <T:Any> Flow<AppState<T>>.applyConnections(
    liveData : Boolean = false,
    enableRetry : Boolean = false,
    nullOnComplete : Boolean = false
) : Flow<AppState<T>> {
    return retryWhen { exception, attempt ->
        emit(AppState.Progress(false))
        delay(100)
//        Timber.e("META ERROR RETRY WHEN : ${exception.message}")
        if (enableRetry) {
            for (i in 10 downTo 1) {
//                Timber.e("retrying request in $i")
                delay(1000)
            }
            enableRetry
        } else {
            false
        }
    }.catch { exception ->
        when (exception) {
            is com.google.gson.stream.MalformedJsonException -> {
//                Timber.e("META ERROR : ${exception.message}")
//                emit(AppState.Error(MetaResponse("Server Error", "", 0)))
            }
            is HttpException -> {
                val errorJsonString = exception.response()?.errorBody()?.string()
                try {
//                    val metaResponse = Gson().fromJson(errorJsonString!!, Meta::class.java)
//                    emit(Resource.Error(metaResponse.meta))
//                    Timber.e("WAS HERE")
//                    Timber.e("META ERROR : ${exception.message}")
                    emit(AppState.Error(errorJsonString.toString()))
                } catch (e: JsonSyntaxException) {
                    if (exception.response()?.code() == 404) {
                        emit(AppState.Error("Page not found"))
//                        emit(
//                            Resource.Error(
//                                MetaResponse(
//                                    "Requested Resource is Not Found",
//                                    "404",
//                                    0
//                                )
//                            )
//                        )
//                        Timber.e("META ERROR : ${exception.message}")
                    } else {
                        emit(AppState.Error("Unsupported Response"))
//                        emit(Resource.Error(MetaResponse("Unsupported Response", "", 0)))
//                        Timber.e("META ERROR : ${exception.message}")
                    }
                }
            }
            is UnknownHostException -> {
                AppState.Error("No Internet Connectivity")
//                emit(Resource.Error(MetaResponse("No Internet Connectivity", "", 0)))
//                Timber.e("META ERROR : ${exception.message}")
            }
            else -> {
                AppState.Error(exception.toString())
//                emit(Resource.Error(MetaResponse(exception.toString(), "", 0)))
//                Timber.e("META ERROR : ${exception.message}")
            }
        }



    }.onStart {
        delay(100)
        emit(AppState.Progress(isLoading = true))
    }.onCompletion {
        delay(100)
        emit(AppState.Progress(isLoading = false))

        if (nullOnComplete) {
            emit(AppState.Success(null))
        }

    }
}
