package com.acdetorres.nidoregistration.dao.model

import androidx.room.PrimaryKey
import java.io.File

data class FormWithFile (
    val timeStamp : String,
    val relationship : String,
    val firstName : String,
    val lastName : String,
    val dob: String,
    val contactNum : String,
    val email: String,
    val numOfChild : String,
    val ages : String,
    val currentBrand : String,
    val signatureName : String,
    val file : File
)