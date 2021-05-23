package com.example.cupofcoffee

import android.app.Application
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.HiltAndroidApp
import se.ansman.kotshi.KotshiJsonAdapterFactory

@HiltAndroidApp
class MainApplication : Application()


@KotshiJsonAdapterFactory
object ApplicationJsonAdapterFactory : JsonAdapter.Factory by KotshiApplicationJsonAdapterFactory
