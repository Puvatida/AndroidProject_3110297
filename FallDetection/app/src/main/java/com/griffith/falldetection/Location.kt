package com.griffith.falldetection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log.e
import android.util.Log.i
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import kotlinx.coroutines.tasks.await
import kotlin.contracts.contract


@Composable
fun LocationScreen(){
    //get user location
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var errMsg by remember { mutableStateOf<String?>(null) }

    val lastLocation = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    //location request permission else -> request wound not be granted
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            granted ->
            if(!granted){
                errMsg = "Denied Permission"
            }
        }
    )

    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED

    //Request Permissoin
    LaunchedEffect(Unit) {
        if(!permission){
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    //fetch locaton
    LaunchedEffect(permission) {
        if(permission) { //if there is permission granted
            try {
                val location = lastLocation.lastLocation.await()
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                } else {
                    errMsg = "Location could not be fetched"
                }//else
            } catch (e: Exception) {
                errMsg = "Error: ${e.message}"
            }
        }
        else{
            errMsg = "Permission for location is not granted"
        }
        }

    //UI section of screen
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
            .padding(32.dp)
    ){
        Text("Current Location")

        Spacer(Modifier.height(20.dp))

        if(userLocation != null){

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLocation!!, 16f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxWidth().height(400.dp),
                cameraPositionState = cameraPositionState
            ){
                Marker(
                    state = MarkerState(position = userLocation!!),
                    title = "Your Location"
                )
            }
        }//if
        else {
            Text(errMsg ?: "Loading... ")
        }
    }
}







