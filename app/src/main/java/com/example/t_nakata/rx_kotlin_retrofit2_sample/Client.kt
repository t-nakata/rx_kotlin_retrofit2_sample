package com.example.t_nakata.rx_kotlin_retrofit2_sample

import android.content.Context
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    companion object {
        private const val BASE_URL = "https://api.syosetu.com/novelapi/api/"
        private lateinit var sRetrofit : Retrofit
    }

    init {
        sRetrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }


    fun getClient(context: Context): Retrofit {
        return sRetrofit
    }
}