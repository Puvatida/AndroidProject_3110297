package com.griffith.falldetection

import android.R.attr.dial
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Tasks.call
import java.lang.ProcessBuilder.Redirect.to


class AlertActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val emergencyNumber = intent.getStringExtra("emergencyNumber") ?: "999"
        setContent {
//            Text("Alert Activity Page")
            AlertScreen(emergencyNumber = emergencyNumber)
        }//setContent
    }//onCreate
}//class
//Alert Screen pop-up Method to be involve when Fall Detected
@Composable
fun AlertScreen(emergencyNumber: String) {
    //set variables
    var countDownTime by remember { mutableStateOf(20) }//give user 10 seconds to react / interact with screen
    var emergencyTime by remember { mutableStateOf(false) }
    val context = LocalContext.current
    //Timer for Emergency call (starts once alert popups)
    LaunchedEffect(Unit) {
        object : CountDownTimer(20_000, 1000){//20 sec, updates every 1seconds
            override fun onTick(msRemaining: Long){
                countDownTime = (msRemaining / 1000).toInt()
            }

            override fun onFinish() {//if no interaction, emergency is true
                emergencyTime = true //emergency mode on to trigger emergency call
            }
        }.start()

    }
    //when timer finish, auto call
    LaunchedEffect(emergencyTime) { //to use delay of 2seconds before launch
        if(emergencyTime){
            kotlinx.coroutines.delay(2000) //to slow things down
            autoCall(context, emergencyNumber)
        }

    }

    //UI - for user to press okay if it is a false alert.
    //add column to space out everything and add spacers
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        )
         {
        Text(
            text = "Fall Detect",
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Are you okay?",
        )
        Spacer(modifier = Modifier.height(20.dp))
        //showing user timer
        Text(
            text = "calling emergency contact in $countDownTime seconds...",
        )
        Spacer(modifier = Modifier.height(40.dp))
        //button to deactivate thr call after trigger
        Button(onClick = {
            //if user interact with button, close the alert sceen
            (context as Activity).finish()
        },
            modifier = Modifier.fillMaxWidth().padding(8.dp).height(55.dp)
        ){
            Text("I'm Ok")
        }
        //I'm not okay -> alert call
//        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = {
            autoCall(context, emergencyNumber)
        },
            modifier = Modifier.fillMaxWidth().padding(8.dp).height(55.dp)
        ) {
            Text("Make Emergency Call")
        }
    }
    }
}
//Function for emergency call
fun autoCall(context: android.content.Context, emergencyNumber: String){ //same dial intent used in homescreen emergency call button.
//    val emergencyNum = "456"//456 for now
//    using intent to make call by dial action
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$emergencyNumber")
    context.startActivity(intent)

    Log.d("AlertActivity", "Timer over, calling emergency contact..")
}