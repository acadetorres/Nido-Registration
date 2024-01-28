package com.acdetorres.nidoregistration

import com.acdetorres.nidoregistration.dao.Form
import com.acdetorres.nidoregistration.dao.RoomDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepository @Inject constructor(val roomDao :RoomDao, val apiService: ApiService ) {


    suspend fun checkConflict(timeStamp : String) = flow {
        emit(AppState.Progress(true))
        val response = roomDao.checkConflict(timeStamp)
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }

    suspend fun insertSubmit(form : Form) = flow {
        emit(AppState.Progress(true))
        val response = roomDao.insertFormEntry(form)
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }

    suspend fun getAllRecords() = flow {
        emit(AppState.Progress(true))
        val response = roomDao.getAllRecords()
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }

    suspend fun getRecordsCount() = flow {
        emit(AppState.Progress(true))
        val response = roomDao.getRecordCount()
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }


    suspend fun deleteAllRecords() = flow {
        emit(AppState.Progress(true))
        val response = roomDao.deleteAllRecords()
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }



}