package com.griffith.falldetection

import android.R.attr.label
import android.content.Intent
import android.telephony.emergency.EmergencyNumber
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Slider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel


import java.lang.Compiler.enable
import kotlin.jvm.java


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = viewModel(), onLogout: () -> Unit){

    val scrollState = rememberScrollState()
    //from view model
    val fullName = settingsViewModel.fullName
    val age = settingsViewModel.age
    val emergencyName = settingsViewModel.emergencyName
    val emergencyNumber = settingsViewModel.emergencyNumber
//    val locationEnabled = settingsViewModel.locationEnabled
//    val notificationEnabled = settingsViewModel.notificationsEnabled
//    val darkModeEnabled = settingsViewModel.darkModeEnabled
    val  editing = settingsViewModel.isEditing
    var logout by remember {mutableStateOf(false)}

    //UI
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(32.dp)
    ){
        Text( //title for screen ---Comments are repeated in this section--
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.size(10.dp))//spacer



        //add scrollable content
        Column(
            modifier = Modifier
                .weight(1f).verticalScroll(scrollState)
        ){
            //Card layout
            Card(
                modifier = Modifier.fillMaxSize()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ){
                //USER PROFILE
// ----------------------------------------
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text( //text for the section
                        text = "User Profile",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    TextField( //setting text field
                        value = fullName,
                        onValueChange = { settingsViewModel.updateFullName(it)}, //set from viewModel function
                        label = {Text("Full name")},
                        modifier = Modifier.fillMaxWidth(),
//                        enable = editing
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    TextField( //setting text field
                        value = age,
                        onValueChange = { settingsViewModel.updateAge(it)}, //set w/viewModel function
                        label = {Text("Age ")},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = editing
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }

            }//CARD: userProfile
//---------------------------------------
            //Card layout
            Card(
                modifier = Modifier.fillMaxSize()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ){
                //Emergency Contact
                //----------------------------------------
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text( //text for the section
                        text = "Emergency Contact",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    TextField(
                        value = emergencyName,
                        onValueChange = { settingsViewModel.updateEmergencyName(it)},
                        label = {Text("Contact Name ")},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = editing
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    TextField(
                        value = emergencyNumber,
                        onValueChange = { settingsViewModel.updateEmergencyNumber(it)},
                        label = {Text("Phone Number ")},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = editing
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }

            }//CARD: Emergency Contact
//---------------------------------------
            //Card layout
            Card(
                modifier = Modifier.fillMaxSize()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ){
                //Simulated fall
                //----------------------------------------
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text( //text for the section
                        text = "Simulated Fall",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "Press the button for testing"
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    simulatedFallButton(editing = editing, emergencyNumber = emergencyNumber)

                }

            }//CARD: Fall Detection button
//---------------------------------------
            //Card layout
//            Card(
//                modifier = Modifier.fillMaxSize()
//                    .padding(vertical = 8.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ){
//                //Permission
//                //----------------------------------------
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text( //text for the section
//                        text = "Permission",
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Spacer(modifier = Modifier.size(8.dp))
//
//                    //location TOGGLE
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ){
//                        Column(
//                            modifier = Modifier.weight(1f)){
//                            Text("Location Access ")
//                            Text(
//                                text = if (locationEnabled)
//                                    "Location is Enabled"
//                                            else
//                                                "Location is disabled",
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                        Switch(
//                            checked = darkModeEnabled,
//                            onCheckedChange = {settingsViewModel.setLocationEnabled(it)}
//                        )
//                    }
//                    Spacer(modifier = Modifier.size(8.dp))
//
//                    //notification toggle
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ){
//                        Column(
//                            modifier = Modifier.weight(1f)){
//                            Text("Fall Notification")
//                            Text(
//                                text = "Show Alert when fall detected",
//                                style = MaterialTheme.typography.bodySmall
//                            )
//                        }
//                        Switch(
//                            checked = notificationEnabled,
//                            onCheckedChange = { settingsViewModel.setNotificationEnabled(it)}
//                        )
//                    }
//                    Spacer(modifier = Modifier.size(8.dp))
//                }
//
//            }//CARD: Permissions
// ---------------------------------------
//            //Card layout
//            Card(
//                modifier = Modifier.fillMaxSize()
//                    .padding(vertical = 8.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ){
//                //App Settings
//                //----------------------------------------
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text( //text for the section
//                        text = "App Settings",
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Spacer(modifier = Modifier.size(8.dp))
//
//                    //Dark MODE TOGGLE
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ){
//                        Column(
//                            modifier = Modifier.weight(1f)){
//                                Text("Dark Mode")
//                                Text(
//                                    text = "Turn on Dark Mode",
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            }
//                            Switch(
//                                checked = darkModeEnabled,
//                                onCheckedChange = { settingsViewModel.darkModeEnabled}
//                            )
//                    }
//                    Spacer(modifier = Modifier.size(8.dp))
//                }
//
//            }//CARD: App Settings

            Button( //app for editing, everything will be disabled before
                onClick = {
                    if (editing){
                        //save
                        settingsViewModel.saveChanges()
                    }
                    else {
                        //edit
                        settingsViewModel.editing()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editing) "Save" else "Edit")
            }

            Button(//button to logout and bring user back to startScreen()
                onClick = {logout = true},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
            if (logout){ //logout dialog to ask user
                AlertDialog(
                    onDismissRequest =  { logout = false },
                    title = { Text("Logout?") },
                    text = { Text("Are you sure?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                logout = false
                                onLogout()
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                logout = false
                            }
                        ) {
                            Text("No")
                        }
                    },
                )
            }//if for logout dialog button

        }//Scrollable

    }//1stUI column

}//settingsScreen()

@Composable
fun simulatedFallButton(editing: Boolean, emergencyNumber: String){
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(context, AccelerometerService::class.java)
            //input the user emergency contact from settings
        intent.putExtra("simulateFall", true) //setting correct key word to call the service
        intent.putExtra("emergencyNumber", emergencyNumber) //saved number
         //using intent for alertAcitvity Screen
        context.startService(intent)
    },
        //do not want user to be able to make a call while editing the settings page.
        enabled = !editing, //enable and disable when editing the settings page
        modifier = Modifier.fillMaxWidth().padding(8.dp).height(55.dp)
    ){
        Text("Simulated Fall")
    }//set width + height
}

