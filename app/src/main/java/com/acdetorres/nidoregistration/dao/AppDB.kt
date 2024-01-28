package com.acdetorres.nidoregistration.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Form::class])
abstract class AppDB : RoomDatabase() {

    abstract fun getRoomDao() : RoomDao

}