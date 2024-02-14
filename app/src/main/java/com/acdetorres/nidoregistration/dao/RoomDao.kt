package com.acdetorres.nidoregistration.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acdetorres.nidoregistration.dao.model.Ambassador
import com.acdetorres.nidoregistration.dao.model.Form
import com.acdetorres.nidoregistration.dao.model.GetProvincesResponse
import com.acdetorres.nidoregistration.dao.model.LoggedOnAmbassador

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

    @Insert(entity = Ambassador::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertAmbassadors(
        ambassador : List<Ambassador>
    ) : List<Long>

    @Query("SELECT * FROM ambassadors")
    fun getAmbassadors() : List<Ambassador>

    @Query("Select * FROM loggedonambassador")
    fun getLoggedOnAmbassador() : LoggedOnAmbassador

    @Query("Delete from loggedonambassador")
    fun deleteLoggedOnAmbassador() : Int

    @Insert(entity = LoggedOnAmbassador::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedOnAmbassador(
        ambassador: LoggedOnAmbassador
    ) : Long

    @Insert(entity = GetProvincesResponse.Province::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertProvinces(
        provinces: List<GetProvincesResponse.Province>
    ) : List<Long>

    @Query("Select * FROM province")
    fun getProvinces() : List<GetProvincesResponse.Province>

}