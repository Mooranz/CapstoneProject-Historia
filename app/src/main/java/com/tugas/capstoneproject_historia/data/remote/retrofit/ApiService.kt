package com.tugas.capstoneproject_historia.data.remote.retrofit

import com.tugas.capstoneproject_historia.data.remote.response.HistoriaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part file: MultipartBody.Part,
    ): Call<HistoriaResponse>
}
