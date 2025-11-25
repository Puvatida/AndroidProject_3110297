package com.griffith.falldetection

import android.R.attr.version
import androidx.room.Database
import androidx.room.RoomDatabase
/*GitHub Link: https://github.com/Puvatida/AndroidProject_3110297*/
@Database(entities = [User::class], version = 1 )
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
}