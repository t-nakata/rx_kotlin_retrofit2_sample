package com.example.t_nakata.rx_kotlin_retrofit2_sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var mObservable: Observable<List<NovelInfo>>
    private lateinit var mSubscription: Subscription


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = Client().getClient(this).create(NarouService::class.java)

        mObservable = service.getNovelInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(3)
    }

    override fun onResume() {
        super.onResume()

        mSubscription = mObservable
                .subscribe (
                        { list -> doSomethingToList(list) }
                        , { t -> onError(t) }
                        , { Log.d("MainActivity", "complete")}
        )
    }

    override fun onPause() {
        super.onPause()

        mSubscription.unsubscribe()
    }

    private fun doSomethingToList(list: List<NovelInfo>) {

        list.forEach {
            Log.d("MainActivity", "$it")
        }
    }

    private fun onError(e: Throwable?) {
        Log.e("MainActivity", "onError" , e)
    }
}
