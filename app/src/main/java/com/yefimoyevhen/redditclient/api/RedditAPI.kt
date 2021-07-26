package com.yefimoyevhen.redditclient.api

import com.yefimoyevhen.redditclient.model.response.AccessToken
import com.yefimoyevhen.redditclient.model.response.Resp
import retrofit2.Response
import retrofit2.http.*

interface RedditAPI {
    @FormUrlEncoded
    @POST()
    suspend fun getAccessToken(
        @Url url: String,
        @Header("Authorization") authHeader: String,
        @Field("grant_type") grantType: String,
        @Field("device_id") deviceId: String
    ): Response<AccessToken>

    @GET("/top.json?limit=50")
    suspend fun getEntries(
        @Header("Authorization") authHeader: String,
    ): Response<Resp>
}