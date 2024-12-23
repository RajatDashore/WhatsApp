package com.example.whatsappclone.Repository

import com.example.whatsappclone.FireBaseClient.FirebaseClient
import com.example.whatsappclone.Modules.DataModel
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firebaseClient: FirebaseClient
) {
    var listener: Listener? = null
    fun login(username: String, password: String, uId: String, isDone: (Boolean, String?) -> Unit) {
        firebaseClient.login(username, password, uId, isDone)
    }

    fun initFirebase() {
        firebaseClient.subscribeForLatestEvent(object : FirebaseClient.Listener {
            override fun onLatesteventListener(event: DataModel) {
                listener?.onLatestEventListener(event)
                when (event.type) {


                    else -> Unit
                }
            }

        })
    }

    interface Listener {
        fun onLatestEventListener(dataModel: DataModel)
    }

}