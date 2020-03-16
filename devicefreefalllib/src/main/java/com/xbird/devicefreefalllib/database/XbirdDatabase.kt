package com.xbird.devicefreefalllib.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Count::class], version = 1)
abstract class XbirdDatabase : RoomDatabase() {

    abstract fun count(): CountDao

    companion object { 
        @Volatile 
        private var INSTANCE: XbirdDatabase? = null
 
        fun getDatabase(context: Context): XbirdDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) { 
                return tempInstance 
            } 
            synchronized(this) { 
                val instance = Room.databaseBuilder(
                        context.applicationContext, 
                        XbirdDatabase::class.java,
                        "FreefallCounter_Db"
                ).fallbackToDestructiveMigration().build() 
                INSTANCE = instance
                return instance 
            } 
        } 
    } 
} 