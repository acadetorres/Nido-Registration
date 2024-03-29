package com.acdetorres.nidoregistration.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acdetorres.nidoregistration.dao.model.Ambassador
import com.acdetorres.nidoregistration.dao.model.Form
import com.acdetorres.nidoregistration.dao.model.GetProvincesResponse
import com.acdetorres.nidoregistration.dao.model.LoggedOnAmbassador

@Database(version = 18, entities = [Form::class, Ambassador::class, LoggedOnAmbassador::class, GetProvincesResponse.Province::class])
abstract class AppDB : RoomDatabase() {

    abstract fun getRoomDao() : RoomDao

}