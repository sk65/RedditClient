package com.yefimoyevhen.redditclient.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.yefimoyevhen.redditclient.R
import com.yefimoyevhen.redditclient.api.RedditAPI
import com.yefimoyevhen.redditclient.database.RedditDao
import com.yefimoyevhen.redditclient.model.Entry
import com.yefimoyevhen.redditclient.util.*
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

class RedditRepository @Inject constructor(
    private val dao: RedditDao,
    private val api: RedditAPI,
    @ApplicationContext val context: Context
) {
    private var afterKey: String? = null

    val resourceLiveData = MutableLiveData<Resource<List<Entry>>>()

    private suspend fun findAllEntriesFromDB() = dao.findAllEntries()

    private suspend fun insertEntry(entry: Entry) = dao.insertEntry(entry)

    private suspend fun deleteAllEntry() = dao.deleteAllEntry()

    suspend fun fetchData(isRefresh: Boolean) {
        resourceLiveData.postValue(Resource.Loading())
        if (hasInternetConnection(context)) {
            if (isRefresh) {
                deleteAllEntry()
                afterKey = null
            }
            refreshData()
        } else {
            resourceLiveData.postValue(Resource.Error(context.getString(R.string.no_internet_connection)))
        }
        val entries = findAllEntriesFromDB()
        resourceLiveData.postValue(Resource.Success(entries))
    }

    private suspend fun refreshData() {
        try {
            val tokenResponse = api.getAccessToken()
            if (tokenResponse.isSuccessful) {
                val accessToken = tokenResponse.body()!!.access_token
                val entriesResponse = api.getEntries(
                    "$BEARER$accessToken",
                    COUNT_OF_PAGE,
                    afterKey)
                if (entriesResponse.isSuccessful) {
                    entriesResponse.body()!!.data.apply {
                        afterKey = after
                        children.forEach { insertEntry(it.convertDataToEntry()) }
                    }
                } else {
                    resourceLiveData.postValue(Resource.Error(tokenResponse.message()))
                }
            } else {
                resourceLiveData.postValue(Resource.Error(tokenResponse.message()))
            }
        } catch (e: HttpException) {
            resourceLiveData.postValue(
                Resource.Error(e.getMessageFromException(context))
            )
        } catch (e: Exception) {
            resourceLiveData.postValue(
                Resource.Error(e.getMessageFromException(context))
            )
        }
    }
}