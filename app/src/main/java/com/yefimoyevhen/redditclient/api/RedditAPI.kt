package com.yefimoyevhen.redditclient.api

import com.yefimoyevhen.redditclient.model.response.AccessToken
import com.yefimoyevhen.redditclient.model.response.EntriesResponse
import com.yefimoyevhen.redditclient.util.*
import okhttp3.Credentials
import retrofit2.Response
import retrofit2.http.*

interface RedditAPI {
    @FormUrlEncoded
    @POST()
    suspend fun getAccessToken(
        @Url url: String = REDDIT_ACCESS_TOKEN_URL,
        @Header("Authorization") authHeader: String
        = Credentials.basic(REDDIT_USERNAME, REDDIT_PASSWORD),
        @Field("grant_type") grantType: String = APP_ONLY_GRANT_TYPE,
        @Field("device_id") deviceId: String = DEVICE_ID
    ): Response<AccessToken>

    @GET("/top.json")
    suspend fun getEntries(
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): Response<EntriesResponse>
}