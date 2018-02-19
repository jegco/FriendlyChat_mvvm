package com.google.firebase.udacity.friendlychat

import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage

/**
 * Created by jorge_caro on 2/8/18.
 */
interface MessageContract {
    interface View{
        fun openPhotoPicker()
        fun applyRetrievedConfiguration()
        fun buttonState(charSequence: CharSequence)
        fun updateMessages(friendlyMessage: FriendlyMessage)
        fun sendMessage()
    }
    interface ViewModel{
        fun fecthConfig()
        fun detachDatabaseReadListener()
        fun sendMessage(newMessage: String)
        fun getRetrievedConfiguration(): Long
    }

}