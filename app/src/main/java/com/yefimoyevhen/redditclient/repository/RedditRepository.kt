package com.yefimoyevhen.redditclient.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.yefimoyevhen.redditclient.api.RedditAPI
import com.yefimoyevhen.redditclient.database.RedditDao
import com.yefimoyevhen.redditclient.model.Entry
import com.yefimoyevhen.redditclient.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Credentials.basic
import javax.inject.Inject

class RedditRepository @Inject constructor(
    private val dao: RedditDao,
    private val api: RedditAPI,
    @ApplicationContext val context: Context
) {

    val resourceLiveData = MutableLiveData<Resource<List<Entry>>>()

    private suspend fun findAllEntriesFromDB() = dao.findAllEntries()

    private suspend fun insertEntry(entry: Entry) = dao.insertEntry(entry)

    private suspend fun getAccessToken() = api.getAccessToken(
        REDDIT_ACCESS_TOKEN_URL,
        basic(REDDIT_USERNAME, REDDIT_PASSWORD),
        APP_ONLY_GRANT_TYPE,
        DEVICE_ID
    )

    private suspend fun deleteAllEntry() = dao.deleteAllEntry()

    private suspend fun findAllEntriesFromInternet(token: String) =
        api.getEntries("Bearer$token")

    suspend fun fetchData() {
        resourceLiveData.postValue(Resource.Loading())
        if (isOnline(context)) {
            refreshData()
        }
        val entries = findAllEntriesFromDB()
        resourceLiveData.postValue(Resource.Success(entries))
    }

    private suspend fun refreshData() {
        val tokenResponse = getAccessToken()
        if (tokenResponse.isSuccessful) {
            val accessToken = tokenResponse.body()?.access_token ?: return
            val entriesResponse = findAllEntriesFromInternet(accessToken)
            if (entriesResponse.isSuccessful) {
                val children = entriesResponse.body()?.data?.children
                children?.let {
                    deleteAllEntry()
                    children.forEach { child ->
                        val entry = convertDataToEntry(child.data)
                        insertEntry(entry)
                    }
                }
            } else {
                resourceLiveData.postValue(Resource.Error(tokenResponse.message()))
            }
        } else {
            resourceLiveData.postValue(Resource.Error(tokenResponse.message()))
        }
    }
}