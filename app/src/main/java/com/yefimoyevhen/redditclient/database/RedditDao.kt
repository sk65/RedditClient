package com.yefimoyevhen.redditclient.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.yefimoyevhen.redditclient.model.Entry

@Dao
interface RedditDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertEntry(entry: Entry)

    @Query("SELECT * FROM entry")
    suspend fun findAllEntries(): List<Entry>

    @Query("DELETE FROM entry")
    suspend fun deleteAllEntry()
}