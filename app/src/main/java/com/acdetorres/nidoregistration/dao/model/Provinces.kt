package com.acdetorres.nidoregistration.dao.model


import com.google.gson.annotations.SerializedName

class Provinces : ArrayList<Provinces.ProvincesItem>(){
    data class ProvincesItem(
        @SerializedName("key")
        val key: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("region")
        val region: String
    )
}