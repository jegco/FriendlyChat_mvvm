package com.google.firebase.udacity.friendlychat.repository.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage


/**
 * Created by jorge_caro on 2/9/18.
 */

@Database(entities = [FriendlyMessage::class] , version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}