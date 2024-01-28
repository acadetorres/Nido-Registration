package com.acdetorres.nidoregistration.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {


    @Query("SELECT timeStamp from forms where timeStamp == :timeStamp")
    fun checkConflict(timeStamp : String) : Int

    @Query("SELECT COUNT('id') from forms")
    fun getRecordCount() : Int

    @Query("SELECT * FROM forms order by timeStamp asc")
    fun getAllRecords() : List<Form>

    @Query("DELETE FROM forms")
    fun deleteAllRecords() : Int

    @Insert(entity = Form::class)
    fun insertFormEntry(
        form: Form
    ) : Long

}