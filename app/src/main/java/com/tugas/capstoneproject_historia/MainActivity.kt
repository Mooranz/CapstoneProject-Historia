package com.tugas.capstoneproject_historia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.tugas.capstoneproject_historia.utils.DateFormatter
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.data.remote.response.Data
import com.tugas.capstoneproject_historia.databinding.ActivityMainBinding
import com.tugas.capstoneproject_historia.ui.camera.CameraActivity
import com.tugas.capstoneproject_historia.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.tugas.capstoneproject_historia.ui.detail.DetailActivity
import com.tugas.capstoneproject_historia.ui.history.HistoryViewModel
import com.tugas.capstoneproject_historia.ui.history.ViewModelFactory
import com.tugas.capstoneproject_historia.utils.reduceFileImage
import com.tugas.capstoneproject_historia.utils.uriToFile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var currentImageUri: Uri? = null

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val actionBar = supportActionBar
        actionBar?.title = "Uploading..."

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        mainViewModel.uploadResult.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.uploadData.observe(this){
            if (it != null) {
                uploadData(it)
            }
        }

        if (currentImageUri == null) {
            startCameraX()
        }

        binding.previewImageView.setOnClickListener {
            if (currentImageUri != null) {
                val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()
                mainViewModel.uploadImage(imageFile, this)
                currentImageUri = null
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (currentImageUri != null) {
                showImage()
                val imageFile = currentImageUri?.let { uriToFile(it, this).reduceFileImage() }
                if (imageFile != null) {
                    mainViewModel.uploadImage(imageFile, this)
                    currentImageUri = null
                }
            } else {
                startCameraX()
            }
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun uploadData(inputData : Data) {
        if (inputData != null) {
            val data = HistoryEntity(
                title = inputData.result,
                date = DateFormatter.formatLongToDate(System.currentTimeMillis()),
                imageUri = inputData.imageUrl,
                confidenceScore = inputData.confidenceScore,
                explanation = inputData.explanation
            )
            historyViewModel.insertHistory(data)
            intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL, data)
            startActivity(intent)
        }
    }

    companion object {
        var currentImageUri: Uri? = null
    }
}