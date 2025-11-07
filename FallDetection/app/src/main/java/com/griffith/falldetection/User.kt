package com.griffith.falldetection

import androidx.room.Entity
import androidx.room.PrimaryKey
//set up user entity that is represented a row in "User" table
//user class with password and email entity in parameter
@Entity(tableName = "users")
data class User (
    @PrimaryKey val email: String,
    val password: String
) {

}