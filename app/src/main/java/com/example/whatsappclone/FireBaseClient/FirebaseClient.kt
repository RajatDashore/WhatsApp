package com.example.whatsappclone.FireBaseClient

import com.example.whatsappclone.Modules.DataModel
import com.example.whatsappclone.Utills.FireBaseFieldName.constants.PASSWORD
import com.example.whatsappclone.Utills.FireBaseFieldName.constants.STATUS
import com.example.whatsappclone.Utills.UserStatus.ONLINE
import com.example.whatsappclone.VideoCall.ForgroundService.MainServiceAction.LATEST_EVENT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val dbref: DatabaseReference,
    private val gson: Gson

) {
    private var currentUserName: String? = null
    private fun setUserName(username: String) {
        this.currentUserName = username
    }

    fun login(username: String, password: String, uId: String, done: (Boolean, String?) -> Unit) {
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // if the current user exist or not
                // if exist chech password

                if (snapshot.hasChild(uId)) {
                    val dbPass = snapshot.child(PASSWORD).value
                    if (password == dbPass) {
                        // pass is correct & sign in
                        dbref.child(username).child(STATUS).setValue(ONLINE).addOnCompleteListener {
                            setUserName(username)
                            done(true, null)
                        }.addOnFailureListener {
                            done(false, it.message)
                        }

                    } else {
                        done(false, "password is wrong")

                    }
                } else {
                    dbref.child(username).child(PASSWORD).setValue(password).addOnCompleteListener {
                        dbref.child(username).child(STATUS).setValue(ONLINE)
                            .addOnCompleteListener {
                                setUserName(username)
                                done(true, null)
                            }.addOnFailureListener {
                                done(false, it.message)
                            }
                    }.addOnFailureListener {
                        done(false, it.message)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    fun subscribeForLatestEvent(param: Listener) {
        try {


        } catch (e: Exception) {

        }
    }

    fun sendMessageToOtherClients(message: DataModel, success: (Boolean) -> Unit) {
        val covertMessage = gson.toJson((message.copy(sender = currentUserName)))
        FirebaseAuth.getInstance().uid?.let {
            dbref.child("Users").child(it).child(LATEST_EVENT.toString())
                .setValue(covertMessage).addOnSuccessListener {

                }.addOnFailureListener {

                }
        }
    }

    interface Listener {
        fun onLatesteventListener(event: DataModel)
    }
}