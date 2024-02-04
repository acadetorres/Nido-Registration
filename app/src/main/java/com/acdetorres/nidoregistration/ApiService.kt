package com.acdetorres.nidoregistration

import com.acdetorres.nidoregistration.dao.model.GetAmbassadorsResponse
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET

interface ApiService {

    @GET("registration_api/get_ambassadors")
    suspend fun getAmbassadors(): GetAmbassadorsResponse
}