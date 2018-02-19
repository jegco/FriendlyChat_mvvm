package com.google.firebase.udacity.friendlychat.root

import com.google.firebase.udacity.friendlychat.Message.view.MainActivity
import com.google.firebase.udacity.friendlychat.Message.viewmodel.MessageViewModel
import com.google.firebase.udacity.friendlychat.repository.local.MessageLocalRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Created by jorge_caro on 2/9/18.
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(target: MainActivity)

    fun inject(target: MessageViewModel)

    fun inject(target: MessageLocalRepository)

}