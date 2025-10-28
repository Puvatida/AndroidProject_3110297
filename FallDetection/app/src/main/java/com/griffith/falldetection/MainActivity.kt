package com.griffith.falldetection

import android.R.attr.onClick
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Text("Set up")
            horizontalDivider()
            emergButton(onClick ={
                //once we onCLick actin

            })//onClick button
            }
        }//set content
    }

@Composable
fun emergButton(onClick: () -> Unit){
    Button(onClick = onClick,
        modifier = Modifier.size(150.dp), //set width + height
        shape = CircleShape //circle shaped button
        ){
        Text("Emergency")
    }
}
@Composable
fun horizontalDivider(){
    Column {
        Text("First ")
        Divider()
        Text("Second")
    }
}