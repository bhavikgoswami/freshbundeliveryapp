package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.ProfileActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.auth.user.User
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.Picker
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.Picker.Companion.PICKED_MEDIA_LIST
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.Picker.Companion.REQUEST_CODE_PICKER
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.utils.PickerOptions
import com.greypixstudio.broovisdeliveryapp.utils.isEmailValid
import com.greypixstudio.broovisdeliveryapp.utils.isPhoneValid
import com.greypixstudio.broovisdeliveryapp.viewmodel.user.UserViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.greypixstudio.broovisdeliveryapp.databinding.ToolbarLayoutBinding
import com.greypixstudio.broovisdeliveryapp.model.auth.getuserdetail.Data
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : BaseActivity() {

    lateinit var binding: ProfileActivityBinding
    private var isRegister: Boolean = false
    private var mobile = ""
    private val userViewModel by viewModel<UserViewModel>()
    var fileToUpload: MultipartBody.Part? = null
    private var fileToUploadFullPath: File? = null
    private val PERMISSION_CHECK_CODE_FOR_STORAGE = 1
    var imagePath = ""
    private var PERMISSION_CODE: Int = 88808
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding
    private var gender = Constants.MALE

    private var type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        init()
      //  getFirebaseToken()
        binding.saveButton.visibility = View.VISIBLE
        binding.updateBtn.visibility = View.GONE

    }

    private fun getFirebaseToken() {

        FirebaseOptions.Builder()
            .setProjectId(Constants.PROJECT_ID)
            .setApplicationId(Constants.APPLICATION_ID)
            .setApiKey(Constants.API_KEY)
            .build()
        Firebase.initialize(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val firebaseToken = task.result

            if (Paper.book().contains(Constants.USER_TOKEN)) {
                setTokenUpdateObserver()
                userViewModel.updateFirebaseToken(firebaseToken)
            }
        })
    }

    private fun setTokenUpdateObserver() {
        if (Utils.checkConnection(this@ProfileActivity)) {
            /**
             * observe for success response from update token
             */
            if (!userViewModel.signUpUserData.hasObservers()) {
                userViewModel.signUpUserData.observe(
                    this,
                    Observer { signupUser ->
                        if (signupUser != null) {
                            if (signupUser.success) {
                                Utils.hideProgress()
                            } else {
                                Toast.makeText(
                                    this@ProfileActivity,
                                    signupUser.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        } else {
            Toast.makeText(
                this@ProfileActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun init() {
        toolbarLayoutBinding = binding.profileToolbar
        binding.profileToolbar.toolbarNametxtView.text = getText(R.string.lbl_profile)
        binding.profileToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 1)
            setGetUserDetailsObserver()
        }

        binding.genderMaleLirLayout.setOnClickListener { genderSelection(binding.genderMaleLirLayout) }
        binding.genderFemaleLirLayout.setOnClickListener { genderSelection(binding.genderFemaleLirLayout) }


        if (intent.hasExtra("register")) {
            isRegister = intent.getBooleanExtra(
                "register", false
            )
            mobile = intent.getStringExtra("mobile").toString()
            binding.mobileEditText.setText(mobile)
        }

        binding.dobEditText.setOnClickListener {
            openDatePicker()
        }

        binding.profilePicImageView.setOnClickListener {
            openGallery()
        }
        binding.userprofileImageView.setOnClickListener {
            openGallery()
        }

        binding.saveButton.setOnClickListener {
            val firstName = binding.firstNameEdtText.text.toString().trim()
            val lastName = binding.lastNameEdtText.text.toString().trim()
            val email = binding.emailEdtView.text.toString().trim()
            val mobile = binding.mobileEditText.text.toString().trim()
            val birthDate = binding.dobEditText.text.toString().trim()
            val alternative_contact_no = binding.alternativeMobileEditText.text.toString()
            val device_type = Constants.DEVICE_TYPE
            val haveLicense = if (binding.cbHaveLicence.isChecked) {
                "Yes"
            } else {
                "No"
            }

            if (firstName.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_firstname))
            } else if (lastName.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_lastname))
            } else if (mobile.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_mobile_number))
            } else if (email.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_email_address))
            } else {
                if (!email.isEmailValid()) {
                    showToast(getString(R.string.mag_please_enter_valid_email_address))
                } else if (!mobile.isPhoneValid()) {
                    showToast(getString(R.string.msg_please_enter_valid_mobile_number))
                }else if (alternative_contact_no.isNotEmpty() && !Utils.verifyPhoneNumber(
                        alternative_contact_no
                    )
                ) {
                    showToast(getString(R.string.msg_please_enter_valid_mobile_number))
                } else if (birthDate.isEmpty()) {
                    showToast(getString(R.string.msg_please_select_birthdate))
                }  else {
                    if (isRegister) {
                        setSignupObserver()
                        if (Utils.checkConnection(this@ProfileActivity)) {
                            if (fileToUploadFullPath != null) {
                                val requestBody =
                                    fileToUploadFullPath?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                requestBody?.let {
                                    fileToUpload = MultipartBody.Part.createFormData(
                                        "image",
                                        fileToUploadFullPath?.name,
                                        it
                                    )
                                }
                                userViewModel.signupUser(
                                    "0".toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    firstName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    lastName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    mobile.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    email.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    birthDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    gender.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    device_type.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    haveLicense.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    fileToUpload,
                                    requestBody
                                )
                            } else {
                                showToast("Please upload profile Image!")
                            }
                        } else {
                            Toast.makeText(
                                this@ProfileActivity,
                                getString(R.string.msg_internet_connection_not_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                    }
                }
            }
        }

        binding.updateBtn.setOnClickListener {
            val firstName = binding.firstNameEdtText.text.toString().trim()
            val lastName = binding.lastNameEdtText.text.toString().trim()
            val email = binding.emailEdtView.text.toString().trim()
            val mobile = binding.mobileEditText.text.toString().trim()
            val birthDate = binding.dobEditText.text.toString().trim()
            val alternative_contact_no = binding.alternativeMobileEditText.text.toString().trim()
            val noOfMember = Constants.BLANK_STRING
            val device_type = Constants.DEVICE_TYPE


            val haveLicense = if (binding.cbHaveLicence.isChecked) {
                "Yes"
            } else {
                "No"
            }
            if (firstName.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_firstname))
            } else if (lastName.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_lastname))
            } else if (email.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_email_address))
            } else if (!email.isEmailValid()) {
                showToast(getString(R.string.mag_please_enter_valid_email_address))
                binding.emailEdtView.requestFocus()
            } else if (mobile.isEmpty()) {
                showToast(getString(R.string.msg_please_enter_mobile_number))
            } else if (!Utils.verifyPhoneNumber(mobile)) {
                showToast(getString(R.string.msg_please_enter_valid_mobile_number))
            } else if (alternative_contact_no.isNotEmpty() && !Utils.verifyPhoneNumber(
                    alternative_contact_no
                )
            ) {
                showToast(getString(R.string.msg_please_enter_valid_mobile_number))
            } else if (birthDate.isEmpty()) {
                showToast(getString(R.string.msg_please_select_birthdate))
            } else {
                updateSignupObserver()
                if (Utils.checkConnection(this@ProfileActivity)) {
                    if (fileToUploadFullPath == null) {
                        userViewModel.userDetailUpdate(
                            firstName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            lastName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            mobile.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            alternative_contact_no.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            email.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            birthDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            noOfMember.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            device_type.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            gender.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            null,
                            null
                        )
                    } else {
                        val requestBody =
                            fileToUploadFullPath?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        requestBody?.let {
                            fileToUpload = MultipartBody.Part.createFormData(
                                "image",
                                fileToUploadFullPath?.name,
                                it
                            )
                        }
                        userViewModel.userDetailUpdate(
                            firstName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            lastName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            mobile.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            alternative_contact_no.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            email.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            birthDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            noOfMember.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            device_type.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            gender.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            fileToUpload,
                            requestBody
                        )
                    }
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        getString(R.string.msg_internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
    }

    private fun setGetUserDetailsObserver() {
        if (Utils.checkConnection(this@ProfileActivity)) {
            userViewModel.getUserDetails()
        } else {
            Toast.makeText(
                this@ProfileActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!userViewModel.getUserDetailData.hasObservers()) {
            userViewModel.getUserDetailData.observe(
                this
            ) { getUserDetailData ->
                if (getUserDetailData != null) {
                    if (getUserDetailData.success) {
                        Utils.hideProgress()
                        getUserDetailData.results.data.let { records ->
                            getUserDetailData.results.data
                            setUserDetails(records)

                        }
                    } else {
                        Toast.makeText(
                            this@ProfileActivity,
                            getUserDetailData.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUserDetails(records: Data) {

        binding.saveButton.visibility = View.GONE
        binding.updateBtn.visibility = View.VISIBLE
        if (records != null) {
            Glide.with(this@ProfileActivity)
                .load(Constants.PREFIX_DOMAIN.plus(records.image))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.userprofileImageView)

            binding.firstNameEdtText.setText(records.firstName)
            binding.lastNameEdtText.setText(records.lastName)
            binding.emailEdtView.setText(records.email)
            binding.mobileEditText.setText(records.primaryContactNo)
            binding.dobEditText.setText(records.birthdate)
            if (records.gender == null || records.gender.equals(Constants.MALE) == true) {
                genderSelection(binding.genderMaleLirLayout)
            } else {
                genderSelection(binding.genderFemaleLirLayout)
            }
            binding.cbHaveLicence.isChecked = records.hasLicense.equals("Yes")

        }

    }

    private fun genderSelection(mLirLayout: LinearLayout) {

        binding.genderMaleLirLayout.setBackgroundResource(R.drawable.shape_gray_rounded_corner)
        binding.genderFemaleLirLayout.setBackgroundResource(R.drawable.shape_gray_rounded_corner)
        binding.maleTxtView.setTextColor(getColor(R.color.color_disable_gray))
        binding.femaleTxtView.setTextColor(getColor(R.color.color_disable_gray))
        binding.maleImgView.setImageResource(R.drawable.ic_gender_male_default)
        binding.femaleImgView.setImageResource(R.drawable.ic_gender_female_default)

        if (mLirLayout == binding.genderMaleLirLayout) {
            gender = Constants.MALE
            binding.genderMaleLirLayout.setBackgroundResource(R.drawable.shape_yellow_rounded_corner_border)
            binding.maleTxtView.setTextColor(getColor(R.color.black))
            binding.maleImgView.setImageResource(R.drawable.ic_gender_male_selected)
        } else if (mLirLayout == binding.genderFemaleLirLayout) {
            gender = Constants.FEMALE
            binding.genderFemaleLirLayout.setBackgroundResource(R.drawable.shape_yellow_rounded_corner_border)
            binding.femaleTxtView.setTextColor(getColor(R.color.black))
            binding.femaleImgView.setImageResource(R.drawable.ic_gender_female_selected)
        }

    }

    private fun updateSignupObserver() {
        /**
         * observe for success response from signup user
         */
        if (!userViewModel.userDetailUpdateData.hasObservers()) {
            userViewModel.userDetailUpdateData.observe(
                this@ProfileActivity,
                Observer { userDetailUpdateData ->
                    Toast.makeText(
                        this@ProfileActivity,
                        getString(R.string.lbl_profile_Update),
                        Toast.LENGTH_SHORT
                    ).show()
                    val activityIntent = Intent(
                        this@ProfileActivity,
                        MainActivity::class.java
                    )
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(activityIntent)
                    overridePendingTransition(0, 0)
                    finish()
                })
        }

        /**
         * observe for failed response from Generate OTP API
         */
        if (!userViewModel.loadingState.hasObservers()) {
            userViewModel.loadingState.observe(this, Observer { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@ProfileActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode
                    }
                }
            })
        }
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissionsList = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            requestPermissions(permissionsList, PERMISSION_CHECK_CODE_FOR_STORAGE)
        } else {
            openPicker()
        }
    }


    private fun openPicker() {
        try {
            val mPickerOptions =
                PickerOptions.init().apply {
                    maxCount = 1
                    maxVideoDuration = 10
                    allowFrontCamera = true
                    excludeVideos = true
                    onlyVideo = false
                }

            Picker.startPicker(this@ProfileActivity, mPickerOptions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CHECK_CODE_FOR_STORAGE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICKER) {
            val mImageList =
                data?.getStringArrayListExtra(PICKED_MEDIA_LIST) as ArrayList //List of selected/captured images/videos
            try {
                CropImage.activity(Uri.parse(mImageList[0])).setAspectRatio(1, 1)
                    .setInitialRotation(0)
                    .setFlipHorizontally(false).setFlipVertically(false)
                    .start(this@ProfileActivity)
            } catch (e: IOException) {
                e.printStackTrace();
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val croppedPath = result.uri.path
                if (croppedPath != null) {
                    val resultUri = result.uri
                    imagePath = result.uri.path!!.toString()
//                    binding.userprofileImageView.setImageBitmap(result.bitmap)
                    fileToUploadFullPath = File(result.uri.path)
                    Glide.with(this@ProfileActivity)
                        .load(imagePath)
                        .error(R.drawable.ic_thumbnail)
                        .placeholder(R.drawable.ic_thumbnail)
                        .into(binding.userprofileImageView)
                    println("onActivityResult: $result")
                    println("onActivityResult: resultUri: $resultUri")
                    println("onActivityResult: imagePath: $imagePath")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun setSignupObserver() {
        /**
         * observe for success response from signup user
         */
        if (!userViewModel.signUpUserData.hasObservers()) {
            userViewModel.signUpUserData.observe(
                this@ProfileActivity,
                Observer { signupUser ->
                    if (signupUser.success) {
                        Utils.hideProgress()
                        Toast.makeText(
                            this@ProfileActivity,
                            getString(R.string.lbl_profile_registered),
                            Toast.LENGTH_SHORT
                        ).show()
                        Paper.book().write(Constants.IS_LOGIN, true)
                        Paper.book().write(Constants.USER_TOKEN, signupUser.results.token)
                        Paper.book().write(Constants.USER_DETAILS, signupUser.results.user)

                        val mIntent = Intent(this@ProfileActivity, MPINActivity::class.java)
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(mIntent)
                        overridePendingTransition(0, 0)
                        finish()

                        binding.saveButton.visibility = View.GONE
                        binding.updateBtn.visibility = View.VISIBLE
                    } else {
                        showToast(signupUser.message)
                    }
                })
        }

        /**
         * observe for failed response from Generate OTP API
         */
        if (!userViewModel.loadingState.hasObservers()) {
            userViewModel.loadingState.observe(this, Observer { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@ProfileActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        val errorCode = errorData!!.errorCode
                        checkErrorCode(errorCode!!)
                    }
                }
            })
        }
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -15)

        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            binding.dobEditText.setText(dateFormat.format(calendar.time))
        }
        val datepicker = DatePickerDialog(
            this@ProfileActivity,
            R.style.datepicker,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datepicker.datePicker.maxDate = calendar.timeInMillis
        datepicker.show()
    }
}