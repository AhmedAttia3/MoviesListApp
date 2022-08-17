package com.smart.movieslist.di.module

import android.content.Context
import androidx.room.Room
import com.smart.movieslist.data.storage.local.db.AppDB
import com.smart.movieslist.data.storage.local.db.AppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideChannelDao(appDatabase: AppDB): AppDao {
        return appDatabase.channelDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDB {
        return Room.databaseBuilder(
            appContext,
            AppDB::class.java,
            "moviesListDB"
        ).allowMainThreadQueries().build()
    }
}