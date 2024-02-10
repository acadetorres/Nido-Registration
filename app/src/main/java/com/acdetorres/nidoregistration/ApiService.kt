package com.acdetorres.nidoregistration

import com.acdetorres.nidoregistration.dao.model.GetAmbassadorsResponse
import com.acdetorres.nidoregistration.dao.model.MetaResponse
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("registration_api/get_ambassadors")
    suspend fun getAmbassadors(): GetAmbassadorsResponse

    @POST("api/user/{user_id}/batch-registration")
    @Multipart
    suspend fun batchSync(
        @Path("user_id") userId : String,
        @Part("fname[]") fname : List<RequestBody>,
        @Part("lname[]") lname : List<RequestBody>,
        @Part("contact_num[]") contactNum : List<RequestBody>,
        @Part("email[]") email : List<RequestBody>,
        @Part("birthday[]") birthday : List<RequestBody>,
        @Part("relationship[]") relationship : List<RequestBody>,
        @Part("number_of_children[]") numOfChildren : List<RequestBody>,
        @Part("current_brand_milk[]") currentBrandOfMilk : List<RequestBody>,
        @Part("registration_etimestamp[]") registration : List<RequestBody>,
        @Part("child_age[][]") childAge : List<RequestBody>,
        @Part signatures : List<MultipartBody.Part>
    ) : MetaResponse
}