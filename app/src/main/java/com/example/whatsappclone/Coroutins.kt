package com.example.whatsappclone

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


fun main() {
    val obj = Coroutins()
    obj.scope()
}

private class Coroutins {
    fun scope() {
        CoroutineScope(Dispatchers.Default).launch {
            Task1()
        }
        CoroutineScope(Dispatchers.Default).launch {
            Task2()
        }
    }

    suspend fun Task1() {
        Log.d("hello", "Starting Task1...")
        yield()
        Log.d(TAG, "Ending Task2...")
    }

    suspend fun Task2() {
        Log.d("hello", "Task2 Starting...")
        yield()
        Log.d("hello", "Task2 Ending")
    }
}