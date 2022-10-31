package com.dicoding.mystory.view.addstory

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystory.R
import com.dicoding.mystory.databinding.ActivityAddStoryBinding
import com.dicoding.mystory.util.createCustomTempFile
import com.dicoding.mystory.util.reduceFileImage
import com.dicoding.mystory.util.uriToFile
import com.dicoding.mystory.factory.ViewModelFactory
import com.dicoding.mystory.factory.ViewModelFactoryStory
import com.dicoding.mystory.model.Result
import com.dicoding.mystory.view.login.LoginActivity
import com.dicoding.mystory.view.main.MainActivity
import com.dicoding.mystory.view.welcome.WelcomeActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStoryActivity : AppCompatActivity() {
    private lateinit var viewModel: AddStoryViewModel
    private val viewModel2: AddViewModel by viewModels {
        ViewModelFactoryStory(this)
    }
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private var token=""
    private var getFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null


    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Can't Get permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[AddStoryViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                token=user.token
                binding.tvGreeting.text =getString(R.string.greeting3, user.name)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

        }

        setupView()
        setupAction()
        playAnimation()
        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage(token) }
    }

    private fun setupView() {
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
    private fun setupAction() {
        with(binding) {
            ibLogout.setOnClickListener {
                Intent(this@AddStoryActivity, LoginActivity::class.java)
                    .apply {
                        startActivity(this)
                        finishAffinity()
                        viewModel.logout()
                    }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, R.string.choose_pict.toString())
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.dicoding.mystory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadImage(token:String)
    {
        val lat: RequestBody?
        val lon: RequestBody?
       getLocation()
        if (location != null){
            lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
            lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())

        }else{
            Toast.makeText(this@AddStoryActivity, resources.getString(R.string.empty_location), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (getFile != null) {
            showLoading(true)
            val file = reduceFileImage(getFile as File)
            val description =
                binding.descEditText.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
             viewModel2.addStory(token,imageMultipart,description,lat,lon).observe(this) { result ->
                if(result != null)
                {
                    when (result)
                    {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success ->{
                            showLoading(false)
                            val data = result.data
                            if (data.error)
                            {
                                AlertDialog.Builder(this).apply {
                                    setTitle("Ooopss!")
                                    setMessage(getString( R.string.signup_failed, data.message))
                                    setNegativeButton("Ok") { _, _ ->
                                        finish()
                                        startActivity(getIntent())
                                    }
                                    create()
                                    show()
                                }
                            }else
                            {
                                showLoading(false)
                                AlertDialog.Builder(this).apply {
                                    setTitle("Yeah!")
                                    setMessage(getString(R.string.story_success))
                                    setPositiveButton("Lanjut") { _, _ ->

                                        Intent(this@AddStoryActivity, MainActivity::class.java)
                                      .apply {
                                            startActivity(this)
                                             }
                                         finish()
                                    }
                                    create()
                                    show()
                                }
                            }

                        } else ->
                                {

                                    AlertDialog.Builder(this).apply {
                                        setTitle("Ooopss!")
                                        setMessage(getString( R.string.story_failed2))
                                        setNegativeButton("Ok") { _, _ ->
                                            finish()
                                            startActivity(getIntent())
                                        }
                                        create()
                                        show()
                                    }
                                }
                    }
                }

            }

        }
    }


//    private suspend fun uploadImage(token:String) {
//        val lat: RequestBody?
//        val lon: RequestBody?
//        getLocation()
//        if (location != null){
//            lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
//            lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
//
//        }else{
//            Toast.makeText(this@AddStoryActivity, resources.getString(R.string.empty_location), Toast.LENGTH_SHORT)
//                .show()
//            return
//        }
//        if (getFile != null) {
//            showLoading(true)
//            val file = reduceFileImage(getFile as File)
//            val description = binding.descEditText.text.toString().toRequestBody("text/plain".toMediaType())
//            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                file.name,
//                requestImageFile
//            )
//
//            val service = ApiConfig.getApiServiceBearer(token).uploadImage( token,imageMultipart, description,lat,lon)
//                service.
//            service.enqueue(object : Callback<FileUploadResponse> {
//                override fun onResponse(
//                    call: Call<FileUploadResponse>,
//                    response: Response<FileUploadResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        showLoading(false)
//                        val responseBody = response.body()
//                        if (responseBody != null && !responseBody.error) {
//                            Toast.makeText(this@AddStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
//                            Intent(this@AddStoryActivity, MainActivity::class.java)
//                                .apply {
//                                    startActivity(this)
//                                }
//                            finish()
//                        }
//                    } else {
//                        showLoading(false)
//                        Toast.makeText(this@AddStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                    showLoading(false)
//                    Toast.makeText(this@AddStoryActivity, "Failed instance Retrofit", Toast.LENGTH_SHORT).show()
//                }
//            })
//        } else {
//            showLoading(false)
//            Toast.makeText(this@AddStoryActivity, R.string.selectpict, Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun playAnimation() {


        val nameTextView = ObjectAnimator.ofFloat(binding.desTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.descEditTextLayout, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(

                nameTextView,
                nameEditTextLayout,


            )
            startDelay = 500
        }.start()
    }

    override fun onStart() {
        super.onStart()
        getLocation()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun getLocation() {
        if  (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        Toast.makeText(this, R.string.empty_location, Toast.LENGTH_SHORT).show()
                    else {
                        this.location =location
                    }

                }

        }


         else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}