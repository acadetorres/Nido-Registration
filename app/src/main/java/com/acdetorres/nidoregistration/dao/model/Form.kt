package com.acdetorres.nidoregistration.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "forms")
data class Form (
    val timeStamp : Long,
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
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    )