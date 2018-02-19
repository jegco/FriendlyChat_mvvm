package com.google.firebase.udacity.friendlychat.root

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.udacity.friendlychat.helpes.InternetHelper
import com.google.firebase.udacity.friendlychat.interactors.MessageInteractor
import com.google.firebase.udacity.friendlychat.repository.firebase.MessageFirebaseRepository
import com.google.firebase.udacity.friendlychat.repository.local.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jorge_caro on 2/9/18.
 */
@Module
class ApplicationModule(private val application: Application) {

    @Provides @Singleton fun provideContext(): Context = application

    @Provides @Singleton fun provideFirebaseStora(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides @Singleton fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides @Singleton fun provideMessageInteractor(internetHelper: InternetHelper
                                                      ,messageFirebaseRepository: MessageFirebaseRepository
                                                      ,messageLocalRepository: MessageLocalRepository): MessageInteractor = MessageInteractor(internetHelper, messageLocalRepository, messageFirebaseRepository)

    @Provides @Singleton fun provideMessageLocalRepository(database: AppDatabase): MessageLocalRepository = MessageLocalRepository(database)

    @Provides @Singleton fun provideMessageFirebaseRepository(firebaseDatabase: FirebaseDatabase,
                                                              firebaseStorage: FirebaseStorage): MessageFirebaseRepository = MessageFirebaseRepository(firebaseDatabase, firebaseStorage)


    @Provides @Singleton fun provideInternetHelper(context: Context) = InternetHelper(context)


    @Provides @Singleton fun providesAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "friendlymessage").allowMainThreadQueries().build()

}