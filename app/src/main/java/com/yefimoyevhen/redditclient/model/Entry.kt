package com.yefimoyevhen.redditclient.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entry(
    @PrimaryKey
    val entryLink: String,
    val subreddit: String,
    val title: String,
    val author: String,
    val postDate: String,
    val thumbnailUrl: String,
    val rate: String,
    val numberOfComments: String
)
