package com.yefimoyevhen.redditclient.model.response

data class Preview(
    val enabled: Boolean,
    val images: List<Image>,
    val reddit_video_preview: RedditVideoPreview
)