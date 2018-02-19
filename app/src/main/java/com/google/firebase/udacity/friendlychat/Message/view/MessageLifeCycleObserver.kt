package com.google.firebase.udacity.friendlychat.Message.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.firebase.database.ChildEventListener

/**
 * Created by jorge_caro on 2/19/18.
 */
class MessageLifeCycleObserver(mChildEventListener: ChildEventListener) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {

    }

}