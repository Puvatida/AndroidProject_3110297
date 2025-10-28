package com.griffith.falldetection

import android.R.attr.onClick
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Text("Set up")
            HomeScreen()
            }
        }//set content
    }

@Composable
fun HomeScreen(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()){
        emergButton(onClick ={
            //once we onCLick actin
            //ad call function here:
        })//onClick button
    }

}//HomeScreen

@Composable
fun NavigationScreen(){

}//NavigationScreen
@Composable
fun emergButton(onClick: () -> Unit){
    Button(onClick = onClick,
        modifier = Modifier.size(150.dp), //set width + height
        shape = CircleShape //circle shaped button
        //change colour here:> colors = ButtonDefaults.buttonColors(containerColor = Color.red)
        ){
        Text("Emergency")// ,color = Color.Green?
    }
}
