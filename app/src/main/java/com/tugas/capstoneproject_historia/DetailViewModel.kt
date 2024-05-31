package com.tugas.capstoneproject_historia

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tugas.capstoneproject_historia.data.retrofit.ApiConfig
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Response

class DetailViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _data = MutableLiveData<WikiResponse>()
    val data: LiveData<WikiResponse> = _data

    init {
        getData()
    }
    fun getData() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getWiki(titles = "Tugu_Yogyakarta")
        client.enqueue(object : retrofit2.Callback<WikiResponse> {
            override fun onResponse(
                call: Call<WikiResponse>,
                response: Response<WikiResponse>
            ) {
                _isLoading.value =false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _data.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WikiResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun removeReferencesAndExternalLinks(htmlContent: String): String {
        val document = Jsoup.parse(htmlContent)

        // Remove the 'References' section
        document.select("h2:contains(References)").nextAll().remove()
        document.select("h2:contains(References)").remove()

        return document.body().html()
    }
    companion object {
        private const val TAG = "MainViewModel"
    }
}