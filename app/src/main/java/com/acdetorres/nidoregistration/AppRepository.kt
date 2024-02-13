package com.acdetorres.nidoregistration

import android.graphics.Bitmap
import com.acdetorres.nidoregistration.dao.model.Form
import com.acdetorres.nidoregistration.dao.RoomDao
import com.acdetorres.nidoregistration.dao.model.Ambassador
import com.acdetorres.nidoregistration.dao.model.FormWithFile
import com.acdetorres.nidoregistration.dao.model.GetProvincesResponse
import com.acdetorres.nidoregistration.dao.model.LoggedOnAmbassador
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import okhttp3.RequestBody.Companion.toRequestBody

class AppRepository @Inject constructor(val roomDao :RoomDao, val apiService: ApiService ) {

//    suspend fun syncRecords(userId : String, forms : List<FormWithFile>) = flow {
//
//        val fNameRequestBody= forms.map {
//            it.firstName.toRequestBody()
//        }
//
//        val lNameRequestBody = forms.map {
//            it.lastName.toRequestBody()
//        }
//
//        val contactNum = forms.map {
//            it.contactNum.toRequestBody()
//        }
//
//        val email = forms.map {
//            it.email.toRequestBody()
//        }
//
//        val birthday = forms.map {
//            it.email.toRequestBody()
//        }
//
//        val relationship = forms.map {
//            it.relationship.toRequestBody()
//        }
//
//        val numOfChild = forms.map {
//            it.numOfChild.toRequestBody()
//        }
//
//        val currentBrandMilk = forms.map {
//            it.currentBrand.toRequestBody()
//        }
//
//        val timeStamp = forms.map {
//            it.timeStamp.toRequestBody()
//        }
//
//
////        val response = apiService.batchSync(
////            userId,
////            fNameRequestBody,
////            lNameRequestBody,
////            contactNum,
////            email,
////            birthday,
////            relationship,
////            numOfChild,
////            currentBrandMilk,
////            timeStamp,
////            childAge,
////            signatures
////
////        )
//    }

    suspend fun insertLocalProvinces(provinces:List<GetProvincesResponse.Province>) = flow {
        val response = roomDao.insertProvinces(provinces)
        emit(response)
    }
    suspend fun getLocalProvince() = flow {
        emit(roomDao.getProvinces())
    }
    suspend fun getProvinces() = flow {
        val response = apiService.getProvinces()
        emit(AppState.Success(response))
    }.applyConnections()
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