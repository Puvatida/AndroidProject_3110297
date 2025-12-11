package com.griffith.falldetection

import android.R.attr.version
import android.content.Context
import androidx.compose.runtime.IntState
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.java

/*GitHub Link: https://github.com/Puvatida/AndroidProject_3110297*/
@Database(entities = [User::class], version = 2, exportSchema = false )
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            //return database if user exists
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "UserDatabase"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

