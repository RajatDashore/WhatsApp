package com.example.whatsappclone

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun main() {
    val obj = Coroutins()
    print("main method")
    obj.scope()
}

class Coroutins {
    fun scope() {
        CoroutineScope(Dispatchers.IO).launch {
            hello()
        }
    }

    suspend fun hello() {
        println("Before")
        println("After")
        delay(1000)
    }

}

