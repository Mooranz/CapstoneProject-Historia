package com.tugas.capstoneproject_historia.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.utils.DateFormatter
import com.tugas.capstoneproject_historia.MainActivity
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.utils.createCustomTempFile
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.data.remote.RemoteDataSource
import com.tugas.capstoneproject_historia.data.remote.response.Data
import com.tugas.capstoneproject_historia.data.remote.response.LandmarkInfo
import com.tugas.capstoneproject_historia.databinding.ActivityCameraBinding
import com.tugas.capstoneproject_historia.utils.reduceFileImage
import com.tugas.capstoneproject_historia.ui.achievement.MapsActivity
import com.tugas.capstoneproject_historia.ui.detail.DetailActivity
import com.tugas.capstoneproject_historia.ui.history.HistoryActivity
import com.tugas.capstoneproject_historia.ui.history.HistoryViewModel
import com.tugas.capstoneproject_historia.ui.history.ViewModelFactory
import com.tugas.capstoneproject_historia.utils.uriToFile


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var currentImageUri: Uri? = null

    private val cameraViewModel by viewModels<CameraViewModel>()

//    private lateinit var imageClassifierHelper: ImageClassifierHelper2
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

    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        cameraViewModel.isLoading.observe(this){
            showLoading(it)
        }

        cameraViewModel.uploadResult.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        cameraViewModel.uploadData.observe(this){
            if (it != null) {
                val data = HistoryEntity(
                    title = it.result,
                    date = DateFormatter.formatLongToDate(System.currentTimeMillis()),
                    imageUri = it.imageUrl,
                    confidenceScore = it.confidenceScore,
                    explanation = it.explanation
                )
                viewModel.insertHistory(data)
                intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL, data)
                startActivity(intent)
            }
        }

/*        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }*/
        binding.captureImage.setOnClickListener {
            takePhoto()
            /*val imageFile = currentImageUri?.let { uriToFile(it, this@CameraActivity).reduceFileImage() }
            if (imageFile != null) {
                cameraViewModel.uploadImage(imageFile, this@CameraActivity)
            }*/
/*
            val data = setupData()
            makeHistory(data)

            intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)*/
        }

        binding.ivHistory.setOnClickListener {
            intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        binding.ivMapsButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.photoPickerButton.setOnClickListener { startGallery() }

        binding.ivMenuButton.setOnClickListener {
            // Initializing the popup menu and giving the reference as current context
            val popupMenu = PopupMenu(this, binding.ivMenuButton)

            // Inflating popup menu from popup_menu.xml file
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_permission -> {
                        requestPermissionLauncher.launch(REQUIRED_PERMISSION)
                    }

                    R.id.menu_switch_camera -> {
                        cameraSelector =
                            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                            else CameraSelector.DEFAULT_BACK_CAMERA
                        startCamera()
                    }
                }
                true
            }
            // Showing the popup menu
            popupMenu.show()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent(this@CameraActivity, MainActivity::class.java)
                    intent.putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                    setResult(CAMERAX_RESULT, intent)
                    finish()
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            val imageFile = currentImageUri?.let { uriToFile(it, this@CameraActivity).reduceFileImage() }
            if (imageFile != null) {
                cameraViewModel.uploadImage(imageFile, this)
            }  // Jika AI model on cloud
//            analyzeImage()  // Jika model on device
/*            intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)*/
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture?.targetRotation = rotation
            }
        }
    }

    private fun setupData(): LandmarkInfo {
        val repo = RemoteDataSource(this)
        val  landmark = repo.getLandmarkInfo()

        return landmark
    }

    private fun makeHistory(inputData: Data) {
        val data = HistoryEntity(
            title = inputData.result,
            date = DateFormatter.formatLongToDate(System.currentTimeMillis()),
            imageUri = null,
            confidenceScore = inputData.confidenceScore,
            explanation = inputData.explanation
        )
        viewModel.insertHistory(data)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)
    }

/*    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper2(
            context = this,
            classifierListener = object : ImageClassifierHelper2.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(this@CameraActivity, error, Toast.LENGTH_SHORT).show()
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

                            binding.title.text = displayResult
                        }
                    }
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(currentImageUri!!)
    }*/

    companion object {
        private const val TAG = "CameraActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200

        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}