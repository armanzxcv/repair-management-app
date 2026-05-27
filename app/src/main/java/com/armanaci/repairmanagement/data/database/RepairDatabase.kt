package com.armanaci.repairmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.armanaci.repairmanagement.data.model.Repair

@Database(entities = [Repair::class], version = 1, exportSchema = false)
abstract class RepairDatabase : RoomDatabase() {
    abstract fun repairDao(): RepairDao

    companion object {
        @Volatile
        private var INSTANCE: RepairDatabase? = null

        fun getDatabase(context: Context): RepairDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RepairDatabase::class.java,
                    "repair_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
