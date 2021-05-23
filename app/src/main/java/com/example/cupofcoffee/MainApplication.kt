package com.example.cupofcoffee

import android.app.Application
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import se.ansman.kotshi.KotshiJsonAdapterFactory

class MainApplication : Application()


@KotshiJsonAdapterFactory
object ApplicationJsonAdapterFactory : JsonAdapter.Factory by KotshiApplicationJsonAdapterFactory


val moshi: Moshi = Moshi.Builder()
    .add(ApplicationJsonAdapterFactory)
    .build()
