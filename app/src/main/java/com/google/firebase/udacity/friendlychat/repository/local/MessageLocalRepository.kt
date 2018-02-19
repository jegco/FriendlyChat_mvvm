package com.google.firebase.udacity.friendlychat.repository.local

import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage
import com.google.firebase.udacity.friendlychat.repository.Repository
import javax.inject.Inject


/**
 * Created by jorge_caro on 2/9/18.
 */
class MessageLocalRepository (val database: AppDatabase) : Repository<FriendlyMessage> {

    override fun query(): List<FriendlyMessage> = database.messageDao().all

    override fun insert(entity: FriendlyMessage) {
        database.messageDao().insertAll(entity)
    }

    override fun update(entity: FriendlyMessage) {
        database.messageDao().update(entity)
    }

    override fun delete(entity: FriendlyMessage) {
        database.messageDao().delete(entity)
    }
}