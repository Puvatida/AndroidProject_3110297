package com.griffith.falldetection


import android.R.attr.onClick
import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShareLocation
//import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*GitHub Link: https://github.com/Puvatida/AndroidProject_3110297*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        //creating instance of the database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "UserDatabase"//database name
        ).build()
        //use abstract method from AppDatabase to get instances of DAO, to interact with database
        val userDao = db.UserDao()
//        val user: List<User> = userDao.getAll()

        setContent {
            Text("Set up")
            StartScreen(userDao)
            }
        }//set content
    }
////variables for email and password
//private var email = mutableStateOf("enter email")
//private var password = mutableStateOf("enter password")
@Composable
fun StartScreen(userDao: UserDao){
    //create a mutable value to track which screen to show based on user's click
    var view by remember { mutableStateOf("start") }

    when(view) {
        "start" -> WelcomeScreen(//check for the button that was clicked
            registerClick = { view = "register" },
            loginClick = { view = "login" }
        )
        //directing user to welcome screen
        "register" -> RegisterScreen(
            userDao = userDao,
            goBack = {view = "start"}) //to start if user wishes
        "login" -> LoginScreen(
            userDao = userDao,
            goBack = {view = "start"})
    }
}
@Composable //with button parameters onClick()
fun WelcomeScreen(registerClick: () -> Unit, loginClick: () -> Unit) { //First screen user sees

    //add column to space out everything and add spacers
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
//            .padding(innerPadding),
    ) {
        //creating big user icon
        Icon(
            imageVector = Icons.Default.AccountCircle,//vector image of default user icon
            contentDescription = "User Icon",
            modifier = Modifier.size(150.dp).padding(bottom = 32.dp) //padding and size
//        tint = TODO()
        )
        //welcome text
        Text(
            text = "Welcome"
        )
        Spacer(modifier = Modifier.size(40.dp))
        //ask user to login or register button
        Button(onClick = registerClick){
            Text("Register")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = loginClick) {
            Text("Login")
        }
    }
}//Welcome Screen
@Composable
fun LoginScreen(goBack: () -> Unit, userDao: UserDao) {
    //empty variables for email, password and message
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    //concurrency Kotlin use coroutines.
    val coroutineScope = rememberCoroutineScope()
    //if login is successful
    var userLogin by remember{mutableStateOf(false)}

    if(userLogin){
        //if user login we pull the home screen, replacing the login screen.
        HomeScreen()
    }
    else {
        Text("LOGIN SCREEN")
        Column( //center and set size
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(30.dp)
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.size(10.dp))
            //setting text bar and button for registration
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val user = userDao.login(email, password)
                        if (user != null) {
                            message = "You're Login"
                            userLogin = true
                        } else {
                            message = "Invalid credentials"
                        }
                    }//coroutine
                }//onClick
            )//button
            {
                Text("Login")
            }

            Button(onClick = goBack) {
                Text("Back")

            }//back button
            Text(message)
        }
    }//else
}//loginScreen
@Composable
fun RegisterScreen(goBack: () -> Unit, userDao: UserDao) {
    //empty variables for email, password and message
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    //concurrency Kotlin use coroutines.
    val coroutineScope = rememberCoroutineScope()
    //to validate the email and password
    val isValidCredential = email.contains("@") && email.contains(".")
    val notEmpty = email.isNotEmpty() && password.isNotEmpty()

    Text("REGISTER SCREEN")
    Column ( //center and set size
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(30.dp)
    ){
        TextField( //text space for email
            value = email,
            onValueChange = {email = it},
            label = {Text("Email")}
        )
        Spacer(modifier = Modifier.size(10.dp))
        TextField(//text space for password
            value = password,
            onValueChange = {password = it},
            label = {Text("Password")}
        )
        Spacer(modifier = Modifier.size(10.dp))
        //setting text bar and button for registration
        Button(
            //enable if field is not empty and email is valid
            enabled = isValidCredential && notEmpty,
            onClick = {
            coroutineScope.launch(Dispatchers.IO){
                userDao.register(User(email = email, password = password))
                message = "You're registered, Please login."
            }

        }){
            Text("Register")
        }
        Button(onClick = goBack){
            Text("Back")
        }
        Text(message)
    }
}
@Composable
fun HomeScreen(){
    var selectedItem by remember { mutableStateOf(0) } //with no logic yet
    val context = LocalContext.current
    //start the fall detection immediately
    LaunchedEffect(Unit) {
        val intent = Intent(context, AccelerometerService::class.java)
        context.startService(intent)
    }
    //Scaffold
    Scaffold(
        bottomBar = {
            BottomNavBar(selectedItem) { index ->
                selectedItem = index
            }
        }
    ) { innerPadding ->
        //main content

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
        .padding(innerPadding),
        ) {
        emergButton()//onClick button
    }
    }
}//HomeScreen
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

//Bottom navigation: https://developer.android.com/develop/ui/compose/components/navigation-bar
@Composable
fun BottomNavBar(selectedIndex: Int, onItemSelected: (Int)-> Unit){ //with selected index for items for now
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 2, onClick = { onItemSelected(2) },
            icon = { Icon(Icons.Default.ShareLocation, contentDescription = "Location")},
            label = { Text("Location") }
        )
        NavigationBarItem(
            selected = selectedIndex == 0, onClick = { onItemSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home")},
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedIndex == 1, onClick = { onItemSelected(1) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings")},
            label = { Text("Settings") }
        )

    }//Navigation bar contents
}//bottomNavBAr

