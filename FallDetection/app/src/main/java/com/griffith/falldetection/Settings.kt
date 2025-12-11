package com.griffith.falldetection

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text

@Composable
fun SettingsScreen(){

    Text("Hiiiii")
}

@Composable
fun simulatedFallButton(){
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(context, AccelerometerService::class.java) //using intent for alertAcitvity Scrren
        //set fall to true
        intent.putExtra("simulateFall", true)
        context.startService(intent)
    },
        modifier = Modifier.fillMaxWidth().padding(8.dp).height(55.dp)
    ){
        Text("Simulated Fall")
    }//set width + height
}

