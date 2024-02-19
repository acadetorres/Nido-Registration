package com.acdetorres.nidoregistration

import com.acdetorres.nidoregistration.dao.model.Form
import com.acdetorres.nidoregistration.dao.RoomDao
import com.acdetorres.nidoregistration.dao.model.Ambassador
import com.acdetorres.nidoregistration.dao.model.GetProvincesResponse
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepository @Inject constructor(val roomDao :RoomDao, val apiService: ApiService ) {

    suspend fun syncRecords(userId : String, forms : List<Form>) = flow {

        val fNameRequestBody= forms.map {
            it.firstName
        }

        val lNameRequestBody = forms.map {
            it.lastName
        }

        val contactNum = forms.map {
            it.contactNum
        }

        val email = forms.map {
            it.email
        }

        val birthday = forms.map {
            it.dob
        }

        val relationship = forms.map {
            it.parent
        }

        val relationshipLabel = forms.map {
            it.relationship
        }

        val numOfChild = forms.map {
            it.numOfChild
        }

        val currentBrandMilk = forms.map {
            it.currentBrand
        }

        val timeStamp = forms.map {
            it.timeStamp
        }


        val childAges = forms.map {
            it.ages
        }
        val provinceId = forms.map {
            it.provinceId
        }

        val city = forms.map {
            it.city
        }

        val barangay = forms.map {
            it.barangay
        }

        val response = apiService.batchSync(
            userId,
            fNameRequestBody,
            lNameRequestBody,
            contactNum,
            email,
            birthday,
            relationship,
            numOfChild,
            currentBrandMilk,
            timeStamp,
            childAges,
            provinceId,
            city,
            barangay,
            relationshipLabel
        )


        emit(AppState.Success(response))
    }.applyConnections()

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

    suspend fun deleteLoggedOnAmbassador() = flow {
        val response = roomDao.deleteLoggedOnAmbassador()
        emit(AppState.Success(response))
    }.applyConnections()


}