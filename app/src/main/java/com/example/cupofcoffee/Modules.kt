package com.example.cupofcoffee

import com.example.cupofcoffee.app.data.network.RedditApi
import com.example.cupofcoffee.helpers.json.ApiResultEmptyStringToNullAdapter
import com.example.cupofcoffee.helpers.log.AndroidLog
import com.example.cupofcoffee.helpers.log.Log
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object HelperModule {

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(ApiResultEmptyStringToNullAdapter)
            .add(ApplicationJsonAdapterFactory)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = BASIC
            })
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.reddit.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideRedditApi(retrofit: Retrofit): RedditApi {
        return retrofit.create(RedditApi::class.java)
    }


    @Provides
    fun provideAndroidLog(): Log {
        return AndroidLog("cup-of-coffee")
    }
}
