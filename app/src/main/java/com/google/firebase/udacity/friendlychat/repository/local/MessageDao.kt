package com.google.firebase.udacity.friendlychat.repository.local

import android.arch.persistence.room.*
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage


/**
 * Created by jorge_caro on 2/9/18.
 */
@Dao
interface MessageDao {
    @get:Query("SELECT * FROM message")
    val all: List<FriendlyMessage>

    @Query("SELECT * FROM message WHERE username LIKE :username LIMIT 1")
    fun findByName(username: String): FriendlyMessage

    @Insert
    fun insertAll(vararg messages: FriendlyMessage)

    @Delete
    fun delete(message: FriendlyMessage)

    @Update
    fun update(message: FriendlyMessage)
}