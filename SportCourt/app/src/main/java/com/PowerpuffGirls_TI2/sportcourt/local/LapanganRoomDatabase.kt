package com.PowerpuffGirls_TI2.sportcourt.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [LapanganEntity::class], version = 1, exportSchema = false)
abstract class LapanganRoomDatabase : RoomDatabase() {
    abstract fun lapanganDao(): LapanganDao

    companion object{

        @Volatile
        private var INSTANCE : LapanganRoomDatabase? = null

        fun getDatabase(context: Context): LapanganRoomDatabase{
            val tmpInstance = INSTANCE
            if (tmpInstance!=null){
                return tmpInstance
            }else{
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LapanganRoomDatabase::class.java,
                    "lapangan.db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }

    }

}
