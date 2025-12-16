package com.griffith.falldetection

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShareLocation

import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

/*GitHub Link: https://github.com/Puvatida/AndroidProject_3110297*/
/*Puvatida Simcharoen 3110297*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        //creating instance of the database
//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java,
//            "UserDatabase"//database name
//        ).build()
        //reconnecting database
        val db = AppDatabase.getInstance(applicationContext)
        //use abstract method from AppDatabase to get instances of DAO, to interact with database
        val userDao = db.UserDao()
//        val user: List<User> = userDao.getAll()

        setContent {
            StartScreen(userDao)
        }//set content
    }
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
            goBack = {view = "start"},
            onLoginSuccess = {view = "home"})
        "home" -> HomeScreen(
            onLogout = {view = "start"}
        )
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
fun LoginScreen(goBack: () -> Unit, userDao: UserDao, onLoginSuccess: () -> Unit) {
    //empty variables for email, password and message
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    //concurrency Kotlin use coroutines.
    val coroutineScope = rememberCoroutineScope()
    //if login is successful
//    var userLogin by remember{mutableStateOf(false)} //dont need anymore
    //settings viewmodel to update the user info in
    val settingsViewModel: SettingsViewModel = viewModel()
//validations logic
    val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val notEmpty = email.isNotEmpty() && password.isNotEmpty()
    val validLogin = validEmail && notEmpty

//    if(userLogin){
//        //if user login we pull the home screen, replacing the login screen.
//        HomeScreen()
//    }
//    else {
        Column( //center and set size
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(30.dp)
        ) {
            TextField(
                value = email,
                onValueChange = { email = it .trim()},
                label = { Text("Email") },
                isError = email.isNotEmpty() && !validEmail
            )
            if (email.isNotEmpty() && !validEmail) {
                Text(
                    text = "Enter a valid email",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.size(10.dp))
            //setting text bar and button for registration
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val user = userDao.login(email, password)
                        if (user != null) {
                            //when user sucessfully login
                            //letSettingsViewModel know which user is in
                            settingsViewModel.setLoggedInEmail(user.email)

                            withContext((Dispatchers.Main)){
                                message = "You're Login"
//                                userLogin = true
                                onLoginSuccess()
                            }
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
//        }
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
    //to validate the email and password logic
    val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() //built in email pattern
    val validPasswordLength = password.length >=8 //must be bigger or equal to 8
    val containsDigits = password.any { it.isDigit() } //must contains digits
    val notEmpty = email.isNotEmpty() && password.isNotEmpty()
    val validPassword = validPasswordLength && containsDigits

    val validCredential = validEmail && validPassword && notEmpty

    Column ( //center and set size
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(30.dp)
    ) {
        TextField( //text space for email
            value = email,
            onValueChange = { email = it.trim() },
            label = { Text("Email") },
            isError = email.isNotEmpty() && !validEmail//invalid email in text not empty
        )

        if (email.isNotEmpty() && !validEmail) {
            Text(
                text = "Enter valid email",
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        TextField(//text space for password
            value = password,
            onValueChange = {password = it},
            label =  {Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )
        //password valid section
        if(password.isNotEmpty() && !containsDigits){
            Text(
                text = "password must be 8 charecter long and contains a digit" ,
                        color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        //setting text bar and button for registration
        Button(
            //enable if field is not empty and email is valid
            enabled = validCredential,
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
fun HomeScreen(onLogout: () -> Unit){
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

        when(selectedItem){
            0 -> HomeContent(innerPadding)
            1 -> Box(modifier = Modifier.padding(innerPadding)){
                SettingsScreen(onLogout = onLogout)
            }
            2 -> LocationScreen()
        }
        //main content
    }
}//HomeScreen
@Composable
fun HomeContent(innerPadding: PaddingValues){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding).padding(32.dp),
    ) {
        emergButton()//onClick button
        Spacer(modifier = Modifier.size(10.dp))
//        simulatedFallButton() //moved to settingsScreen
    }
}
@Composable
fun emergButton(){ //a dialer will appear upon click (for now)
    val activity = LocalContext.current
    //calling viewModel that deals with user info data
    val settingsViewModel: SettingsViewModel = viewModel()
    //calling emergency number that user set in settings
    val emergencyNumber = settingsViewModel.emergencyNumber

    //removing the hard code "123" number and insert the stored emergency number instead
    Button(onClick = {
//        val num = "123"
        val emergencyNum = if (emergencyNumber.isNotBlank()){
            emergencyNumber //user input number
        }
        else {
            "112" //irish emergency number
        }
        //the user input number
        val intent = Intent(Intent.ACTION_DIAL, "tel: $emergencyNum".toUri()) //using intent for phone call
        activity.startActivity(intent)//start intent
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


