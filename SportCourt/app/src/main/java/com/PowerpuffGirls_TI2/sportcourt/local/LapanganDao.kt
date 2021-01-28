package com.PowerpuffGirls_TI2.sportcourt.local

import androidx.room.*

@Dao
interface LapanganDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(lapangan: LapanganEntity)

    @Update
    fun update(lapangan: LapanganEntity)

    @Delete
    fun delete(lapangan: LapanganEntity)

    @Query("SELECT * from LapanganEntity WHERE id_peminjam = :id_user")
    fun getAllLapangan(id_user : String): List<LapanganEntity>
}