package com.xbird.devicefreefalllib

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.xbird.devicefreefalllib.database.Count
import com.xbird.devicefreefalllib.database.XbirdDatabase
import com.xbird.devicefreefalllib.executors.AppExecutors
import org.greenrobot.eventbus.EventBus


class FreefallService : Service(), SensorEventListener {
    val CHANNEL_ID = "FREE_FALL_CHANNEL"
    val DEVICE_FALL_EVENT_CHANNEL_ID = "DEVICE_FALL_EVENT_FREE_FALL_CHANNEL"
    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null
    var last_x:Float = 0.0f
    var last_y:Float = 0.0f
    var last_z:Float = 0.0f
    var lastUpdate : Long = 0L
    private val SHAKE_THRESHOLD = 800
    override fun onCreate() {
        super.onCreate()
        // Get sensor manager on starting the service.
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Registering...
        mSensorManager?.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
        // Get default sensor type
        mSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int { // Get sensor manager on starting the service.
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Registering...
        mSensorManager?.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
        // Get default sensor type
        mSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        createForegroundServiceNotificationChannel()
        createDeviceFallNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Device Free Fall")
            .setContentText("Device free fall service is running")
            .setSmallIcon(R.drawable.stat_notify_sync)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not Yet Implemented")
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        mSensorManager!!.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
        val mySensor = sensorEvent.sensor
        mSensorManager!!.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL)
        if (mySensor.type == Sensor.TYPE_ACCELEROMETER) {
            var isShaking = false
            val curTime = System.currentTimeMillis()
            if ((curTime - lastUpdate) > 100) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime

                var axisX: Float = sensorEvent.values[0]
                var axisY: Float = sensorEvent.values[1]
                var axisZ: Float = sensorEvent.values[2]
                var at = Math.sqrt(((axisX*axisX) + (axisY * axisY) + (axisZ * axisZ)).toDouble())
                val speed: Float = Math.abs(axisX + axisY + axisZ - last_x - last_y - last_z) / diffTime * 10000
                Log.d("getShakeDetection", "speed: $speed")
                if (speed > SHAKE_THRESHOLD) {
                    isShaking = true
                }
                if(at <= 1 && !isShaking){
                    Log.e("Vector sum> ",""+at)
                    Toast.makeText(this,"Free Fall detect", Toast.LENGTH_LONG).show()
                    var mBuilder =  NotificationCompat.Builder(this,DEVICE_FALL_EVENT_CHANNEL_ID)
                    mBuilder.setSmallIcon(R.drawable.stat_notify_sync)
                    mBuilder.setContentTitle("Device fall Event")
                    mBuilder.setContentText("Your device fall event recorded successfully...")
                    val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    mNotificationManager.notify(Math.random().toInt(), mBuilder.build())
                    AppExecutors.getInstance().diskIO().execute {
                        var countObj = Count(0, 1L)
                        XbirdDatabase.getDatabase(this).count().insert(countObj)
                        /*var localBroadcastManager = LocalBroadcastManager.getInstance(this)
                        var intent = Intent("PERFORM_UPDATE")
                        intent.putExtra("type","update")
                        localBroadcastManager.sendBroadcast(intent)*/
                        EventBus.getDefault().postSticky("update")
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

    private fun createForegroundServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Device free fall Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createDeviceFallNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                DEVICE_FALL_EVENT_CHANNEL_ID,
                "Device free fall Event detected...",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}