package com.griffith.falldetection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
/*GitHub Link: https://github.com/Puvatida/AndroidProject_3110297*/
/*
set up useer DAO (Data access object, provides methods to interact with the user data table
 */
//interface allow program to communicate with the database
@Dao
interface UserDao {
    @Query("SELECT * FROM users") //table name
    fun getAll(): List<User> //get all

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun register(user: User)

    //query for user name and password from registered "user" table
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    //LIMIT 1 in sql: retrieves only the first row from the result set.
    fun login(email: String,  password: String): User?

    @Delete
    fun delete(user: User)
}