package com.smart.movieslist.data.storage.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smart.movieslist.data.model.MovieModel

@Database(
    entities = [
        MovieModel::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDB : RoomDatabase() {
    abstract fun channelDao(): AppDao
}