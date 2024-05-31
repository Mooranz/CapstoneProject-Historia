package com.tugas.capstoneproject_historia.data.retrofit

import com.tugas.capstoneproject_historia.WikiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api.php")
    fun getWiki(
        @Query("action") action: String = "query",
        @Query("titles") titles: String,
        @Query("prop") prop: String = "extracts|pageimages|info",
        @Query("pithumbsize") pithumbsize: String = "400",
        @Query("inprop") inprop: String = "url",
        @Query("redirects") redirects: String = "",
        @Query("format") format: String = "json",
//        @Query("origin") origin: String = "*"
    ): Call<WikiResponse>

}