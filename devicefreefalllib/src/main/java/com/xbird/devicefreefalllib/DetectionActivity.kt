package com.xbird.devicefreefalllib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.xbird.devicefreefalllib.database.CounterViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


open class DetectionActivity : AppCompatActivity() {

    lateinit var countViewModel: CounterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, FreefallService::class.java))
        }else{
            startService(Intent(this, FreefallService::class.java))
        }
        val fallDetectionFactory = FallDetectionModelFactory()
        countViewModel = ViewModelProviders.of(this, fallDetectionFactory).get(CounterViewModel::class.java)
        countViewModel.getCount(this)
    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
    }

    override fun onResume() {
        super.onResume()
        //LocalBroadcastManager.getInstance(this).registerReceiver(listener,  IntentFilter("PERFORM_UPDATE"))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().removeAllStickyEvents()
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun updateEvent(event:String){
        if(event=="update"){
            if(countViewModel!=null && ::countViewModel.isInitialized){
                countViewModel.getCount(this)
            }
        }
    }

  /*  private val listener = MyBroadcastReceiver()

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            when (intent.action) {
                "PERFORM_UPDATE" -> {
                    val data = intent.getStringExtra("type")
                    Log.d("Your Received data : ", data)
                    Toast.makeText(context, "Local broadcast recievr", Toast.LENGTH_LONG).show()
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }

        }
    }*/
}
