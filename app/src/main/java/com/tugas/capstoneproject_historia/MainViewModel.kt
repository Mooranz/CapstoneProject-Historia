package com.tugas.capstoneproject_historia

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tugas.capstoneproject_historia.MainActivity.Companion.currentImageUri
import com.tugas.capstoneproject_historia.data.remote.response.Data
import com.tugas.capstoneproject_historia.data.remote.response.HistoriaResponse
import com.tugas.capstoneproject_historia.data.remote.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class MainViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResult = MutableLiveData<String>()
    val uploadResult: LiveData<String> = _uploadResult

    private val _uploadData = MutableLiveData<Data?>()
    val uploadData: LiveData<Data?> = _uploadData

    fun uploadImage(imageFile: File, context: Context) {
        _isLoading.value = true

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().uploadImage(multipartBody)
        client.enqueue(object : retrofit2.Callback<HistoriaResponse> {
            override fun onResponse(
                call: Call<HistoriaResponse>,
                response: Response<HistoriaResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _uploadResult.value = responseBody.message
                        if (responseBody.data != null) {
                            _uploadData.value = responseBody.data
                            currentImageUri = null
                        }
                    }
                } else {
                    Toast.makeText(context, response.body()?.message ?: "Terjadi kesalahan. Ulangi Lagi.", Toast.LENGTH_SHORT).show()
                    currentImageUri = null
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<HistoriaResponse>, t: Throwable) {
                Toast.makeText(context, t.message ?: "Terjadi kesalahan. Ulangi lagi.", Toast.LENGTH_SHORT).show()
                _isLoading.value = false
                val intent = Intent(context, MainActivity::class.java)
                currentImageUri = null
                context.startActivity(intent)
            }
        })
    }
}