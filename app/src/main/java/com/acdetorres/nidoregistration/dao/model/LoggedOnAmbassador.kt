package com.acdetorres.nidoregistration.dao.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "LoggedOnAmbassador")
data class LoggedOnAmbassador(
    @SerializedName("fname")
    val fname: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("lname")
    val lname: String = "",
    @SerializedName("active")
    val active: String = "",
    @PrimaryKey
    @SerializedName("id")
    val id: String = "",
    @SerializedName("mname")
    val mname: String = "",
    @SerializedName("email")
    val email: String = ""){

    fun toAmbassador() : Ambassador {
        return (Ambassador(fname, password, lname, active, id, mname, email))
    }

}
