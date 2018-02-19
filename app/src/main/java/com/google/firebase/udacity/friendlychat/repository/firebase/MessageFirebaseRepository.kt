package com.google.firebase.udacity.friendlychat.repository.firebase

import android.net.Uri
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage

/**
 * Created by jorge_caro on 2/8/18.
 */
class MessageFirebaseRepository(private val mFirebaseDatabase: FirebaseDatabase, private var mFirebaseStorage: FirebaseStorage) {

    private var mDatabaseReference: DatabaseReference
    private var mStorageReference: StorageReference

    init {
        mDatabaseReference = mFirebaseDatabase.reference.child("message")
        mStorageReference = mFirebaseStorage.reference.child("chat_photos")
    }

     fun sendMessage(message: FriendlyMessage) {
            mDatabaseReference.push().setValue(message)
    }

    fun setChildEvent(childEventListener: ChildEventListener){
        mDatabaseReference.addChildEventListener(childEventListener)
    }

    fun removeChildEvent(childEventListener: ChildEventListener) {
            mDatabaseReference.removeEventListener(childEventListener)
    }

    fun getStorageReference(imageUri: Uri) = mStorageReference.child(imageUri.lastPathSegment)
}