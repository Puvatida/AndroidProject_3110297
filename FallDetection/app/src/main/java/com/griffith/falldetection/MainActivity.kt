package com.griffith.falldetection

import android.R.attr.onClick
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            Text("Set up")
            val navController = rememberNavController()
            HomeScreen(navController)
            }
        }//set content
    }

@Composable
fun HomeScreen(navController : NavController){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()){
        emergButton()//onClick button
//        BottomNavBar()
        Text(text = "Home")
        Button(onClick = { navController.navigate("menu") }) { //must use lambda
            Text(text = "Go to Menu")
        }
        Button(onClick = { navController.navigate("Settings") }){
            Text(text = "Settings")
        }
    }
}//HomeScreen

@Composable
fun NavigationBar(){

}//NavigationScreen
@Composable
fun emergButton(){ //a dialer will appear upon click (for now)
    val activity = LocalContext.current
    Button(onClick = {
        val num = "123"
        val intent = Intent(Intent.ACTION_DIAL) //using intent for phone call
        intent.data = "tel:$num".toUri()
        if (activity != null) {
            activity.startActivity(intent)
        }
    },
        modifier = Modifier.size(150.dp), //set width + height
        shape = CircleShape //circle shaped button
        //change colour here:> colors = ButtonDefaults.buttonColors(containerColor = Color.red)
        ){
        Text("Emergency")// ,color = Color.Green?
    }
}//emergButton

@Composable
fun BottomNavBar(){ //with scaffold
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()){
        NavHost(
            navController = navController,
            startDestination = "home"
        ){
            //composable to switch between them
            composable("home"){
                HomeScreen(navController)
            }
            composable("settings"){
                HomeScreen(navController)//homescreen for now
            }
        }
    }//surface
}
