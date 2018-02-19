package com.google.firebase.udacity.friendlychat.Message.viewmodel

import android.arch.lifecycle.ViewModel
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage
import com.google.firebase.udacity.friendlychat.MessageContract
import com.google.firebase.udacity.friendlychat.interactors.MessageInteractor
import com.google.firebase.udacity.friendlychat.root.App
import java.util.*
import javax.inject.Inject

/**
 * Created by jorge_caro on 2/8/18.
 */
class MessageViewModel : ViewModel(), MessageContract.ViewModel {

    lateinit var mUsername: String
    @Inject lateinit var interactor: MessageInteractor
    lateinit var mChildEventListener: ChildEventListener
    var mFirebaseAuth: FirebaseAuth
    var mFirebaseRemoteConfig: FirebaseRemoteConfig
    var message = ""
    var imageUrl : Uri? = null
    lateinit var friendlyMessage: FriendlyMessage

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        App.component.inject(this)

        val firebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build()
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings)
        val defaultConfigMap = HashMap<String, Any>()
        defaultConfigMap.put(DEFAULT_LENGHT, DEFAULT_MSG_LENGTH_LIMIT)
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap)
    }

    override fun fecthConfig() {
        var cacheExpiration: Long = 3600
        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener {
            mFirebaseRemoteConfig.activateFetched()
            getRetrievedConfiguration()
        }.addOnFailureListener { Log.e("ErrorRemoteConfig", "Error fetching configuration remotly") }
    }

    override fun detachDatabaseReadListener() {
            //interactor.removeChildListener(mChildEventListener)
    }

    override fun sendMessage(newMessage: String) {
        if (imageUrl == null){
            friendlyMessage = FriendlyMessage(newMessage, mUsername, "")
            interactor.sendMessage(friendlyMessage)
        }else{
            val photoReference = interactor.getStorageReference(imageUrl!!)
            photoReference.putFile(imageUrl as Uri).addOnSuccessListener { taskSnapshot ->
                val downloadUrl = taskSnapshot.downloadUrl
                message = downloadUrl.toString()
                friendlyMessage = FriendlyMessage("", mUsername, message)
                interactor.sendMessage(friendlyMessage)
            }

        }
        message = ""
        imageUrl = null
    }

    override fun getRetrievedConfiguration() = mFirebaseRemoteConfig.getLong(DEFAULT_LENGHT)

    fun addChildListener(childEventListener: ChildEventListener){
        mChildEventListener = childEventListener
        interactor.setChildListener(mChildEventListener)
    }

    companion object {
        val DEFAULT_LENGHT = "DEFAULT_MSG_LENGTH_LIMIT"
        val DEFAULT_MSG_LENGTH_LIMIT = 1000
    }

}