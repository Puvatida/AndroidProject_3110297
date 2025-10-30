package com.griffith.falldetection

import android.R.attr.icon
import android.R.id.icon
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
//import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri



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
        emergButton()//onClick button
        val selectedIndex = 0
        val onItemsSelected = 0
//        BottomNavBar(selectedIndex = selectedIndex)
//        Text(text = "Home")
//        Button(onClick = { navController.navigate("menu") }) { //must use lambda
//            Text(text = "Go to Menu")
//        }
//        Button(onClick = { navController.navigate("Settings") }){
//            Text(text = "Settings")
//        }
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
fun BottomNavBar(selectedIndex: Int, onItemSelected: (Int)-> Unit){ //with selected index for items for now
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0, onClick = { onItemSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home")},
            label = { Text("Home") }
        )
    }
}
