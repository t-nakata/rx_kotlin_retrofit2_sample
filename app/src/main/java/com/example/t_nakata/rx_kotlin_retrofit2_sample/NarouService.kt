package com.example.t_nakata.rx_kotlin_retrofit2_sample

import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable


interface NarouService {

    @GET("https://api.syosetu.com/novelapi/api/")
    fun getNovelInfo(@Query("out") out: String = "json",
                     @Query("of") of: String = "t-n-u-w-ga-e",
                     @Query("lim") limit: Int = 200,
                     @Query("order") order: String = "weekly"
    ): Observable<List<NovelInfo>>

}