package com.acdetorres.nidoregistration.dao.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GetProvincesResponse(
    @SerializedName("data")
    val `data`: List<Province>,
    @SerializedName("meta")
    val meta: Meta
) {
    @Entity(tableName = "province")
    data class Province(
        @SerializedName("active")
        val active: String,
        @PrimaryKey
        @SerializedName("id")
        val id: String,
        @SerializedName("provCode")
        val provCode: String,
        @SerializedName("provDesc")
        val provDesc: String,
        @SerializedName("psgcCode")
        val psgcCode: String,
        @SerializedName("regCode")
        val regCode: String
    )

    data class Meta(
        @SerializedName("code")
        val code: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("status")
        val status: Int
    )
}