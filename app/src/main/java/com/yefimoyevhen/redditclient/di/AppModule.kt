package com.yefimoyevhen.redditclient.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yefimoyevhen.redditclient.R
import com.yefimoyevhen.redditclient.api.RedditAPI
import com.yefimoyevhen.redditclient.database.RedditDatabase
import com.yefimoyevhen.redditclient.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_no_img)
            .error(R.drawable.ic_no_img)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun provideRedditDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, RedditDatabase::class.java,
        "todo_db"
    ).build()

    @Singleton
    @Provides
    fun provideRedditDao(
        db: RedditDatabase
    ) = db.redditDao()


    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String, client: OkHttpClient): RedditAPI =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RedditAPI::class.java)

}