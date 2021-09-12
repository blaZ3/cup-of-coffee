package com.example.cupofcoffee

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import com.example.cupofcoffee.app.data.network.RedditApi
import com.example.cupofcoffee.app.data.store.usersettings.AppUserSettingsDataStore
import com.example.cupofcoffee.app.data.store.usersettings.UserSettingsDataStore
import com.example.cupofcoffee.app.data.store.usersettings.userSettingsDataStore
import com.example.cupofcoffee.app.proto.UserSettings
import com.example.cupofcoffee.helpers.json.ApiResultEmptyStringToNullAdapter
import com.example.cupofcoffee.helpers.log.AndroidLog
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.test.Hooks
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

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
        return createOkHttp()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(Hooks.okHttpClient ?: client)
            .baseUrl(Hooks.baseUrl ?: "https://www.reddit.com")
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

    @Provides
    @IODispatcher
    fun providesIODispatcher(): CoroutineDispatcher {
        return IO
    }

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    @UserSettingsAndroidDS
    fun providesUserSettingsDataStore(@ApplicationContext context: Context): DataStore<UserSettings> {
        return context.userSettingsDataStore
    }
}

@Module
@InstallIn(ViewComponent::class)
object DataStoreModule {
    @Provides
    fun provideAppUserSettingsDataStore(
        @UserSettingsAndroidDS ds: DataStore<UserSettings>
    ): UserSettingsDataStore {
        return AppUserSettingsDataStore(ds)
    }
}

@VisibleForTesting
fun createOkHttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
        .readTimeout(120, SECONDS)
        .writeTimeout(120, SECONDS)
        .connectTimeout(30, SECONDS)
        .build()
}
