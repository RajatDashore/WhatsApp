package com.example.whatsappclone.VideoCall.Utills

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context.applicationContext

    @Provides

    fun provideGson(): Gson = Gson()

    @Provides
    fun providesDataBaseInstance(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    fun provideDataBaseReferance(db: FirebaseDatabase): DatabaseReference = db.reference
}