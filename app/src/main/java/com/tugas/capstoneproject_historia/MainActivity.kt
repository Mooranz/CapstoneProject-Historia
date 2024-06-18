package com.tugas.capstoneproject_historia

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.Manifest
import android.icu.text.NumberFormat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.asclepius.utils.DateFormatter
import com.tugas.capstoneproject_historia.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.tugas.capstoneproject_historia.databinding.ActivityMainBinding
import com.tugas.capstoneproject_historia.ui.camera.CameraActivity
//import com.tugas.capstoneproject_historia.ui.camera.ImageClassifierHelper
//import org.tensorflow.lite.task.vision.classifier.Classifications

// Obsolete
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

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

        /*
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         */

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        } else {
            Toast.makeText(this, "Camera is our main feature, please reopen the app until we implement permission request button :)", Toast.LENGTH_SHORT).show()
        }

        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener {
//            analyzeImage()
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
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
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
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
    }
}