package com.yefimoyevhen.redditclient.model.response

data class AccessToken(
    val access_token: String,
    val device_id: String,
    val expires_in: Int,
    val scope: String,
    val token_type: String
)