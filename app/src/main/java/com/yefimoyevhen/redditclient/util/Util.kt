package com.yefimoyevhen.redditclient.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.icu.text.CompactDecimalFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import com.yefimoyevhen.redditclient.R
import com.yefimoyevhen.redditclient.model.Entry
import com.yefimoyevhen.redditclient.model.response.Children
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

fun convertNumberToShortFormat(number: Int, locale: Locale = Locale.US): String {
    val compactDecimalFormat =
        CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT)
    return compactDecimalFormat.format(number.toLong())
}

fun convertDate(timestamp: Double): String {
    val myLong = timestamp.toLong() * 1000
    return PrettyTime().format(Date(myLong))
}

internal fun Children.convertDataToEntry(): Entry {
    val entryLink = "$REDDIT_URL${data.permalink}"
    val subreddit = data.subreddit
    val title = data.title
    val author = data.author
    val postDate = convertDate(data.created_utc)
    val thumbnailUrl = data.thumbnail //TODO cash an image to external storage
    val rate = convertNumberToShortFormat(data.score)
    val numberOfComments = convertNumberToShortFormat(data.num_comments)
    return Entry(
        entryLink,
        subreddit,
        title,
        author,
        postDate,
        thumbnailUrl,
        rate,
        numberOfComments
    )
}

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
        }
    }
    return false
}

internal fun Exception.getMessageFromException(context: Context) =
    localizedMessage ?: context.getString(R.string.something_goes_wrong)
