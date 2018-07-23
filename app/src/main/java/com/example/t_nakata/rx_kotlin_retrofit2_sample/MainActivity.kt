package com.example.t_nakata.rx_kotlin_retrofit2_sample

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mObservable: Observable<List<NovelInfo>>
    private lateinit var mDisposable: Disposable


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

        mDisposable = mObservable
                .subscribe(
                        { list -> doSomethingToList(list.filter { it.allcount == 0 }) }
                        , { t -> onError(t) }
                        , { Log.d("MainActivity", "complete") }
                )
    }

    override fun onPause() {
        super.onPause()

        mDisposable.dispose()
    }

    private fun doSomethingToList(list: List<NovelInfo>) {

        list.forEach {
            Log.d("MainActivity", "$it")
        }

        recycle_view.adapter = NovelInfoAdapter(this, list)
        recycle_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    private fun onError(e: Throwable?) {
        Log.e("MainActivity", "onError", e)
    }

    inner class NovelInfoAdapter(private val context: Context,
                                 private val novelInfoList: List<NovelInfo>)
        : RecyclerView.Adapter<NovelInfoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovelInfoViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val mView = layoutInflater.inflate(R.layout.list_item_novel_info, parent, false)

            return NovelInfoViewHolder(mView)
        }

        override fun getItemCount(): Int {
            return novelInfoList.size
        }

        override fun onBindViewHolder(holder: NovelInfoViewHolder, position: Int) {
            holder?.let {
                it.mTitle.text = "$position : ${novelInfoList[position].title}"
            }
        }
    }

    inner class NovelInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTitle: TextView = view.findViewById(R.id.titleView)

    }
}
