package com.acdetorres.nidoregistration.dao.model


import com.google.gson.annotations.SerializedName

data class MetaResponse(
    @SerializedName("meta")
    val meta: Meta
) {
    data class Meta(
        @SerializedName("code")
        val code: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("status")
        val status: Int
    )
}