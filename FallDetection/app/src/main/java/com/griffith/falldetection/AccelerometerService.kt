package com.griffith.falldetection
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import kotlin.jvm.java
import kotlin.math.sqrt

class AccelerometerService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    //var for detecting impact: define the spike force for possible fall

    //var for inactivity: create the threshold,start time and duration time of inactivity before alert is triggered
    //a spike in magnitude that pay indicate a possible fall to look out for
    private val fallRate = 40.0f //value does not change
    //set to false atm.
    private var fallDetected = false //value may change
    //these values is to determine what is a fall. Magnitude should spike and pause when someone fall.
    //set inactivity for fall at 30.0float
    private val inactivityTime = 2.0f //value may not change
    //set inactivity start time from 0
    private var inactivityStartTime: Long = 0
    //time to wait out for
    private val inactiveDuratiuon =  3000 // 3 seconds
    //define message for log
//    private val logMessage = "AccelerometerService"
    override fun onCreate(){
        super.onCreate()
        //get sensors and components from the system.
        //create an instance of the SensorManager class by calling getSystemService()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //accelerometer sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        //first, always check if sensor is on device.
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            //success
            Log.d( "SensorService","Accelerometer sensor found on this device.")
        } else {
           Log.d("SensorService", "Error, no Accelerometer sensor found on this device.")
        }
        //Register the Listener for fall event.
        sensorManager.registerListener(
            this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL
        )
        Log.d("SensorService", " Accelerometer Service starts in background")
    }
    override fun onBind(p0: Intent?): IBinder? {
       //not need
        return null
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //not need; invokes ANY changes in accuracy VALUES of the sensor object
    }

    override fun onSensorChanged(event : SensorEvent?) { //calls when there is a new sensor event
        if(event == null) return
        //get X,Y and Z acceleration sensor
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        //calculate tota magnitude force with formula
        val magnitude = sqrt(x*x + y*y + z*z)
        Log.d("SensorService","Magnitude:  $magnitude")
        //Detect the sudden impact of magnitude
        if(magnitude>fallRate && !fallDetected){
            fallDetected = true //set the dall detected to equal true
            inactivityStartTime = System.currentTimeMillis()//start at that second from 0
            Log.d("SensorService", "Impact has been detected!")
        }
        //Detect inactivity; then trigger first with activity alert and then in if else statement impact = false
        if(fallDetected){
            if (magnitude < inactivityTime){
                val currentSecond = System.currentTimeMillis()
                if (currentSecond - inactivityStartTime > inactiveDuratiuon ) {//so if it is 3 seconds +
                    Log.d("SensorService", "fall Detected")
                    //launch the alert notification screen when the magnitude have been confirms and the pause has occurred more than 3 seconds
                    val intent = Intent(this, AlertActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//alert to this intent page
                    startActivity(intent)//initiate

                    //set the fallDetected back to false for next alert
                    fallDetected = false
                }
            }
            else {
                //if there is no significant pasuse system assume there is no fall.
                fallDetected = false
            }
        }
    }

}
