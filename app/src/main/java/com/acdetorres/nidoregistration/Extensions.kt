package com.acdetorres.nidoregistration

import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
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
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

fun EditText.chooseDate(context : Context, yearsRestrict : Int = 0) {
    val cal = Calendar.getInstance()

    val maxDate = cal.time.time - (31556952000 * yearsRestrict)

    DatePickerDialog(context,
        { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(cal, this)
        },
        // set DatePickerDialog to point to today's date when it loads up
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)).also {
        if (maxDate != 0.toLong()) {
            it.datePicker.maxDate = maxDate
        }
        it.show()
    }

}

private fun updateDateInView(cal: Calendar, editText : EditText) {
    val myFormat = "MM/dd/yyyy"
//        val myFormat = "MM/dd/yyyy" // mention the format you need
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    editText.setText(sdf.format(cal.time))
//            viewModel.to = sdf.format(cal.time)
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
        Log.d("EXCEPTION", exception.message.toString())
        when (exception) {
            is com.google.gson.stream.MalformedJsonException -> {
//                Timber.e("META ERROR : ${exception.message}")
//                emit(AppState.Error(MetaResponse("Server Error", "", 0)))
                emit(AppState.Error("Server Error"))
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
                emit(AppState.Error("No Internet Connectivity"))
//                emit(Resource.Error(MetaResponse("No Internet Connectivity", "", 0)))
//                Timber.e("META ERROR : ${exception.message}")
            }
            is ConnectException -> {
                emit(AppState.Error("No Internet Connectivity"))
            }
            else -> {
                emit(AppState.Error(exception.toString()))
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



fun convertDate(dateInMilliseconds: Long, dateFormat: String?): String {
    return DateFormat.format(dateFormat, dateInMilliseconds).toString()
}