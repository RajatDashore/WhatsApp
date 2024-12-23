package com.example.whatsappclone.VideoCall.ForgroundService

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.whatsappclone.Modules.DataModel
import com.example.whatsappclone.R
import com.example.whatsappclone.Repository.MainRepository
import com.example.whatsappclone.VideoCall.ForgroundService.MainServiceAction.START_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainService : Service(), MainRepository.Listener {
    private var isServiceRunning = false
    private var userName: String? = null

    @Inject
    lateinit var mainRepository: MainRepository

    private lateinit var notificationManager: NotificationManager


    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(
            NotificationManager::class.java
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let { incomingIntent ->
            when (incomingIntent?.action) {
                START_SERVICE.name -> handleStartService(incomingIntent)

                else -> Unit
            }
        }

        return START_STICKY
    }

    private fun handleStartService(incomingIntent: Intent) {
        //Start our foreground Service
        if (!isServiceRunning) {
            isServiceRunning = true
            userName = incomingIntent.getStringExtra("username")
            startServiceWithNotification()
            mainRepository.listener = this
            mainRepository.initFirebase()

        } else {

        }
    }

    @SuppressLint("ForegroundServiceType")
    private fun startServiceWithNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationchannel = NotificationChannel(
                "channel1", "Foreground", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationchannel)
            val notification = NotificationCompat.Builder(
                this, "channel1"
            ).setSmallIcon(R.drawable.c_1)
            startForeground(1, notification.build())

        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLatestEventListener(dataModel: DataModel) {

    }
}