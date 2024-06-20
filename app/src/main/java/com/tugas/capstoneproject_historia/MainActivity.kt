package com.tugas.capstoneproject_historia

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.Manifest
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.asclepius.utils.DateFormatter
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.data.remote.response.Data
import com.tugas.capstoneproject_historia.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.tugas.capstoneproject_historia.databinding.ActivityMainBinding
import com.tugas.capstoneproject_historia.ui.camera.CameraActivity
import com.tugas.capstoneproject_historia.ui.detail.DetailActivity
import com.tugas.capstoneproject_historia.ui.history.HistoryViewModel
import com.tugas.capstoneproject_historia.ui.history.ViewModelFactory
import com.tugas.capstoneproject_historia.utils.reduceFileImage
import com.tugas.capstoneproject_historia.utils.uriToFile

//import com.tugas.capstoneproject_historia.ui.camera.ImageClassifierHelper
//import org.tensorflow.lite.task.vision.classifier.Classifications

// Obsolete
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var currentImageUri: Uri? = null

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private val mainViewModel by viewModels<MainViewModel>()

//    private lateinit var imageClassifierHelper: ImageClassifierHelper


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

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

/*        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

/*        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            Toast.makeText(this, "Camera is our main feature, please reopen the app until we implement permission request button :)", Toast.LENGTH_SHORT).show()
        }*/

/*        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener {
//            analyzeImage()
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }*/
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

/*
    private fun uploadImage(imageFile: File, context: Context) {
        showLoading(true)

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
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(this@MainActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        if (responseBody.data != null) {
                            uploadData(responseBody.data)
                            currentImageUri = null
                        }
                        Log.d("TestApi", responseBody.message.toString())
                    }
                } else {
                    Toast.makeText(context, response.body()?.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    currentImageUri = null
                }
            }

            override fun onFailure(call: Call<HistoriaResponse>, t: Throwable) {
                Log.d("TestApi", t.message.toString())
                showLoading(false)
                currentImageUri = null
            }
        })
    }*/

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

/*    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                }
                override fun onResult(results: List<Classifications>?) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val sortedCategories =
                                it[0].categories.sortedBy { it?.label }

                            val displayResult =
                                "${sortedCategories.first().label} " + NumberFormat.getPercentInstance()
                                    .format(sortedCategories.first().score) + " | ${sortedCategories[1].label } " + NumberFormat.getPercentInstance()
                                    .format(sortedCategories[1].score)
                        }
                    }
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(currentImageUri!!)
    }*/

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        var currentImageUri: Uri? = null
    }
}