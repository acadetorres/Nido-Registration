package com.acdetorres.nidoregistration

import com.acdetorres.nidoregistration.dao.model.GetAmbassadorsResponse
import com.acdetorres.nidoregistration.dao.model.GetProvincesResponse
import com.acdetorres.nidoregistration.dao.model.Meta
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("registration_api/provinces")
    suspend fun getProvinces() : GetProvincesResponse

    @GET("registration_api/get_ambassadors")
    suspend fun getAmbassadors(): GetAmbassadorsResponse

    @POST("user/{user_id}/batch-registration")
    @FormUrlEncoded
    suspend fun batchSync(
        @Path("user_id") userId: String,
        @Field("fname[]") fname: List<String>,
        @Field("lname[]") lname: List<String>,
        @Field("contact_num[]") contactNum: List<String>,
        @Field("email[]") email: List<String>,
        @Field("birthday[]") birthday: List<String>,
        @Field("relationship[]") relationship: List<String>,
        @Field("number_of_children[]") numOfChildren: List<String>,
        @Field("current_brand_milk[]") currentBrandOfMilk: List<String>,
        @Field("registration_etimestamp[]") registration: List<String>,
        @Field("child_age[]") childAge: List<String>,
        @Field("province_id[]") provinceId: List<String>,
        @Field("city[]") city: List<String>,
        @Field("barangay[]") barangay: List<String>,
        @Field("relationship_label[]") relationshipLabel: List<String>,
        @Field("registration_type[]") registrationType: List<String>,
        @Field("doctors_name[]") doctorsName : List<String>,
        @Field("hospital_name[]") hospitalNames : List<String>,
    ) : Meta
}