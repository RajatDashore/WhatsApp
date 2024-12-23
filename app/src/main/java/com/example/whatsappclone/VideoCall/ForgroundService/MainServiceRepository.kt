package com.example.whatsappclone.VideoCall.ForgroundService

import android.content.Context
import android.content.Intent
import android.os.Build
import javax.inject.Inject

class MainServiceRepository @Inject constructor(
    private val context: Context
) {


    fun startService(userName: String) {
        Thread {
            val intent = Intent(context, MainService::class.java)
            intent.putExtra("username", userName)
            intent.action = MainServiceAction.START_SERVICE.name
            startServiceIntent(intent)
        }.start()
    }


    private fun startServiceIntent(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}