package com.yefimoyevhen.redditclient.database

import androidx.room.Database

import androidx.room.RoomDatabase
import com.yefimoyevhen.redditclient.model.Entry

@Database(
    entities = [Entry::class],
    version = 1,
    exportSchema = false
)
abstract class RedditDatabase : RoomDatabase() {
    abstract fun redditDao(): RedditDao
}