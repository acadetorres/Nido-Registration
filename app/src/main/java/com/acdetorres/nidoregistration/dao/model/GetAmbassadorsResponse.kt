package com.acdetorres.nidoregistration.dao.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Meta(@SerializedName("code")
                val code: String = "",
                @SerializedName("message")
                val message: String = "",
                @SerializedName("status")
                val status: Int = 0)

@Entity(tableName = "ambassadors")
data class Ambassador(@SerializedName("fname")
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
                    val email: String = "") {
    fun toLoggedOnAmbassador() : LoggedOnAmbassador {
        val loggedOnAmbassador = LoggedOnAmbassador(
            fname, password, lname, active, id, mname, email
        )
        return loggedOnAmbassador
    }
}


data class GetAmbassadorsResponse(@SerializedName("data")
                                 val data: List<Ambassador>?,
                                 @SerializedName("meta")
                                 val meta: Meta)


