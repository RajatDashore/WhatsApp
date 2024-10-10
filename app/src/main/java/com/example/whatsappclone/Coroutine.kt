package com.example.whatsappclone

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


// This code is not the part of the project but it is how i can implement the coroutins into the project

fun main() {
    val obj = Coroutine()
    obj.scope()
}

private class Coroutine {
    fun scope() {
        CoroutineScope(Dispatchers.Default).launch {
            task1()
        }
        CoroutineScope(Dispatchers.Default).launch {
            task2()
        }
    }

    suspend fun task1() {
        Log.d("hello", "Starting Task1...")
        yield()
        Log.d(TAG, "Ending Task2...")
    }

    suspend fun task2() {
        Log.d("hello", "Task2 Starting...")
        yield()
        Log.d("hello", "Task2 Ending")
    }
}