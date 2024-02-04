package com.acdetorres.nidoregistration

import com.acdetorres.nidoregistration.dao.model.Form
import com.acdetorres.nidoregistration.dao.RoomDao
import com.acdetorres.nidoregistration.dao.model.Ambassador
import com.acdetorres.nidoregistration.dao.model.LoggedOnAmbassador
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepository @Inject constructor(val roomDao :RoomDao, val apiService: ApiService ) {


    suspend fun getLoggedOnAmbassador() = flow {
        val response = roomDao.getLoggedOnAmbassador()
        emit(AppState.Success(response))
    }

    suspend fun insertLoggedOnAmbassador(ambassador: Ambassador) = flow {
        val response = roomDao.insertLoggedOnAmbassador(ambassador.toLoggedOnAmbassador())
        emit(AppState.Success(response))
    }

    suspend fun getAmbassadors() = flow {
        emit(AppState.Progress(true))
        val response = apiService.getAmbassadors()
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }.applyConnections()


    suspend fun getLocalAmbassadors() = flow {
        val response = roomDao.getAmbassadors()
        emit(AppState.Success(response))
    }

    suspend fun insertAmbassadors(ambassadors : List<Ambassador>) = flow {
        emit(AppState.Progress(true))
        val response = roomDao.insertAmbassadors(ambassadors)
        emit(AppState.Success(response))
        emit(AppState.Progress(false))
    }

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