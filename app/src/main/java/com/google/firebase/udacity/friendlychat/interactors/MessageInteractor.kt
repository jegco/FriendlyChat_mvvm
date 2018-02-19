package com.google.firebase.udacity.friendlychat.interactors

import android.net.Uri
import com.google.firebase.database.ChildEventListener
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage
import com.google.firebase.udacity.friendlychat.helpes.InternetHelper
import com.google.firebase.udacity.friendlychat.repository.firebase.MessageFirebaseRepository
import com.google.firebase.udacity.friendlychat.repository.local.MessageLocalRepository

/**
 * Created by jorge_caro on 2/9/18.
 */
class MessageInteractor(private var internetHelper: InternetHelper
                        ,private var localRepository: MessageLocalRepository
                        ,private var firebaseRepository: MessageFirebaseRepository) {
    fun getMessages(){

    }

    fun sendMessage(message: FriendlyMessage){
        localRepository.insert(message)
        if(internetHelper.checkInternet())
        firebaseRepository.sendMessage(message)
    }

    fun updateMessage(message: FriendlyMessage){

    }

    fun deleteMessage(message: FriendlyMessage){

    }

    fun setChildListener(mChildEventListener: ChildEventListener){
        firebaseRepository.setChildEvent(mChildEventListener)
    }

    fun removeChildListener(mChildEventListener: ChildEventListener){
        firebaseRepository.removeChildEvent(mChildEventListener)
    }

    fun getStorageReference(imageUri: Uri) = firebaseRepository.getStorageReference(imageUri)

}