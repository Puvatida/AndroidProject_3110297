package com.griffith.falldetection

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
/*GitHub Link: https://github.com/Puvatida/AndroidProject_3110297*/
/*Puvatida Simcharoen 3110297*/
//this class is to hold settings in memory and settingsScreen reads from this via uiSate and update functions
class SettingsViewModel (application: Application) : AndroidViewModel(application){

    private val userDao = AppDatabase.getInstance(application).UserDao()

    //current user and app states
    var currentEmail: String? = null
        private set

    var isEditing by mutableStateOf(false)
        private set
    //set user settings
    var fullName by mutableStateOf("")
        private set
    var age by mutableStateOf("")
        private set

    var emergencyName by mutableStateOf("")
        private set
    var emergencyNumber by mutableStateOf("")
        private set

    //after USER login
    fun setLoggedInEmail(email: String){
        currentEmail = email //let the login email to equal this var

        //load and the data from Database to the text field
        viewModelScope.launch(Dispatchers.IO){
            val user = userDao.getUserByEmail(email) //function query from Dao
            if (user != null){ //if there's user login
                withContext(Dispatchers.Main){
                    fullName = user.fullName.orEmpty()
                    age = user.age.orEmpty()
                    emergencyName = user.emergencyName.orEmpty()
                    emergencyNumber = user.emergencyNumber.orEmpty()
                }
            }
        }
    }//setLoggedInEmail

    fun editing(){
        isEditing = true
    }
    fun saveChanges(){
        saveUserToDataBase()
        isEditing = false //set back to false
    }


    //functions for the update
    fun updateFullName(value: String){
        fullName = value
//        saveUserToDataBase()
    }
    fun updateAge(value: String){
        age = value
//        saveUserToDataBase()
    }
    fun updateEmergencyName(value: String){
        emergencyName = value
//        saveUserToDataBase()
    }
    fun updateEmergencyNumber(value: String){
        emergencyNumber = value
//        saveUserToDataBase()
    }
//    fun setLocationEnabled(enabled: Boolean){
//        locationEnabled = enabled
//    }
//    fun setNotificationEnabled(enabled: Boolean){
//        notificationsEnabled = enabled
//    }
//    fun setDarkModeEnabled(enabled: Boolean){
//        darkModeEnabled = enabled
//    }



    //save input from user to database
    private fun saveUserToDataBase(){
        val email = currentEmail ?: return //if no user we save to database

        viewModelScope.launch(Dispatchers.IO){
            val existing = userDao.getUserByEmail(email) //if they exists

            //if they are just update the input field from user
            if(existing != null ){ //they have to be because they have to login to use settings
                val updated = existing.copy(
                    fullName = fullName,
                    age = age,
                    emergencyName = emergencyName,
                    emergencyNumber = emergencyNumber
                )
                userDao.upDateUser(updated)//quary from DAO to update straight to database
            }
        }
    }//saveUsertoDB
}