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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.DrivingLicenseUploadActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.Picker
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.utils.PickerOptions
import com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.greypixstudio.broovisdeliveryapp.databinding.ToolbarLayoutBinding
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getlincencedetail.Records
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import io.paperdb.Paper
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class DrivingLicenseUploadActivity : BaseActivity() {

    private lateinit var binding: DrivingLicenseUploadActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()

    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    private var licenseFrontMultipart: MultipartBody.Part? = null
    private var licenseFrontSideFile: File? = null
    private var licenseBackMultipart: MultipartBody.Part? = null
    private var licenseBackSideFile: File? = null

    private val PERMISSION_CHECK_CODE_FOR_STORAGE = 1
    private var photoTypeRequest: Int = 1

    private var type: Int = 1
    private var getDrivingLicenseDetail = Constants.DOCUMENT_TYPE_LICENCE

    private var selectedLicenceType = Constants.BLANK_STRING
    private var imagePath = Constants.BLANK_STRING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_driving_license_upload)
        init()
        val licenseTypeArray = resources.getStringArray(R.array.LicenceType)

        binding.licenseDetailUpdateBtn.visibility = View.GONE
        binding.licenseDetailSaveBtn.visibility = View.VISIBLE


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, licenseTypeArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.licenseTypeSpinner.adapter = adapter

        binding.licenseTypeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedLicenceType = licenseTypeArray[position]
                //  Toast.makeText(this@DrivingLicenseUploadActivity,""+selectedLicenceType,Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.registrationDateEdtView.setOnClickListener {
            //datePickerRequest = 1
            openDatePickerRegistrationDate()
        }
        binding.expiryDateEdtView.setOnClickListener {
            //datePickerRequest = 2
            openDatePickerExpiryDate()
        }
        open class DrivingNumberTextWatcher(private val editText: EditText) : TextWatcher {
            private val isDelete = false
            private var lastContentLength = 0
            private var contentLength = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                ////Log.d("lastContent","before : "+ s.length.toString())
                contentLength = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                //The requirement is 130 1234 4567, the fourth and fifth digit spaces in the middle are preceded by spaces
                val sb = StringBuffer(s)
                //StringBuffer.length() is the length, so the subscript starts from 1
                //If it is not a space character, insert a space character in front of it

                ////Log.d("lastContent","after : "+ s.length.toString())

                if (s.length >= 5) {
                    val chars = s.toString().toCharArray()
                    //The number subscript starts at 0
                    if (chars[4] != ' ') {
                        sb.insert(4, ' ')
                        setContent(sb)
                    }
                }
                sb.toString()
                /*if (s.length >= 5) {
                    val chars = s.toString().toCharArray()
                    //The number subscript starts at 0
                    if (chars[4] != ' ' && chars[4] != ' ' && !Character.isLetter(chars[4])) {
                        sb.insert(4, ' ')
                        setContent(sb)
                    } else {

                        if (contentLength < s.length) {
                            if (s.length == 5) {
                                sb.insert(5, ' ')
                                setContent(sb)
                            }
                            if (s.length == 10) {
                                if (s.contains("-")) {
                                    sb.insert(10, ' ')
                                    setContent(sb)
                                }
                            }
                        }
                    }
                }*/
            }

            /* Add or remove space EditText settings*/

            private fun setContent(sb: StringBuffer) {
                editText.setText(sb.toString())
                //Move the cursor to the back
                editText.setSelection(sb.length)
            }
        }

        binding.licenceNumberEdtView.addTextChangedListener(object :
            DrivingNumberTextWatcher(binding.licenceNumberEdtView) {
            override fun afterTextChanged(s: Editable) {
                super.afterTextChanged(s)

            }
        })

        binding.licenseDetailSaveBtn.setOnClickListener {
            val documentType = Constants.DOCUMENT_TYPE_LICENCE
            val deliveryUsername = binding.userNameEdtView.text.toString().trim()
            val number = binding.licenceNumberEdtView.text.toString().trim()
            val licenceRegisterDate = binding.registrationDateEdtView.text.toString().trim()
            val licenceExpiryDate = binding.expiryDateEdtView.text.toString().trim()
            val licenseType = selectedLicenceType
            if (deliveryUsername.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_username))
            } else if (number.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_licenceNumber))
            } else if (!Utils.isValidLicenceNumber(number)) {
                showToast(getString(R.string.lbl_Please_enter_licenceNumber_validation))
            } else if (licenceRegisterDate.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_licence_registration_date))
            } else if (licenceExpiryDate.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_licence_expiry_date))
            } else if (licenseType.isEmpty()) {
                showToast(getString(R.string.lbl_Please_select_licence_type))
            } else {
                uploadLincenceDocumnetObserver()
                if (Utils.checkConnection(this@DrivingLicenseUploadActivity)) {
                    if (licenseFrontSideFile != null) {
                        val frontSiderequestBody =
                            licenseFrontSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        frontSiderequestBody?.let {
                            licenseFrontMultipart = MultipartBody.Part.createFormData(
                                "front_side",
                                licenseFrontSideFile?.name,
                                it
                            )
                        }
                        if (licenseBackSideFile != null) {
                            val backSiderequestBody =
                                licenseBackSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            backSiderequestBody?.let {
                                licenseBackMultipart = MultipartBody.Part.createFormData(
                                    "back_side",
                                    licenseBackSideFile?.name,
                                    it
                                )
                            }
                            if (frontSiderequestBody != null && backSiderequestBody != null) {
                                uploadDocumentViewModel.addLicenseDetailResponse(
                                    documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    licenseType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    deliveryUsername.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    number.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    licenceRegisterDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    licenceExpiryDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    licenseFrontMultipart,
                                    frontSiderequestBody,
                                    licenseBackMultipart,
                                    backSiderequestBody
                                )
                            }
                        }
                    } else {
                        showToast(getString(R.string.lbl_Please_upload_licence_Imag))
                    }
                } else {
                    Toast.makeText(
                        this@DrivingLicenseUploadActivity,
                        getString(R.string.msg_internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.licenseDetailUpdateBtn.setOnClickListener {
            val documentType = Constants.DOCUMENT_TYPE_LICENCE
            val deliveryUsername = binding.userNameEdtView.text.toString().trim()
            val number = binding.licenceNumberEdtView.text.toString().trim()
            val licenceRegisterDate = binding.registrationDateEdtView.text.toString().trim()
            val licenceExpiryDate = binding.expiryDateEdtView.text.toString().trim()
            val licenseType = selectedLicenceType
            if (licenseFrontSideFile == null || licenseBackSideFile == null) {
                showToast(getString(R.string.lbl_please_upload_licence_image))
            } else if (deliveryUsername.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_username))
            } else if (number.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_licenceNumber))
            } else if (!Utils.isValidLicenceNumber(number)) {
                showToast(getString(R.string.lbl_Please_enter_licenceNumber_validation))
            } else if (licenceRegisterDate.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_licence_registration_date))
            } else if (licenceExpiryDate.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_licence_expiry_date))
            } else if (licenseType.isEmpty()) {
                showToast(getString(R.string.lbl_Please_select_licence_type))
            } else {
                updateLincenceDocumnetObserver()
                if (Utils.checkConnection(this@DrivingLicenseUploadActivity)) {
                    if (licenseFrontSideFile == null && licenseBackSideFile == null) {
                        uploadDocumentViewModel.updateLicenseDetailResponse(
                            documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenseType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            deliveryUsername.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            number.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenceRegisterDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenceExpiryDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            null,
                            null,
                            null,
                            null
                        )
                    } else {
                        val frontSiderequestBody =
                            licenseFrontSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        frontSiderequestBody?.let {
                            licenseFrontMultipart = MultipartBody.Part.createFormData(
                                "front_side",
                                licenseFrontSideFile?.name,
                                it
                            )
                        }
                        val backSiderequestBody =
                            licenseBackSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        backSiderequestBody?.let {
                            licenseBackMultipart = MultipartBody.Part.createFormData(
                                "back_side",
                                licenseBackSideFile?.name,
                                it
                            )
                        }
                        uploadDocumentViewModel.updateLicenseDetailResponse(
                            documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenseType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            deliveryUsername.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            number.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenceRegisterDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenceExpiryDate.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            licenseFrontMultipart,
                            frontSiderequestBody,
                            licenseBackMultipart,
                            backSiderequestBody
                        )

                    }

                } else {
                    Toast.makeText(
                        this@DrivingLicenseUploadActivity,
                        getString(R.string.msg_internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        binding.licenceFrontUploadBtn.setOnClickListener {
            photoTypeRequest = 1
            openGallery()
        }
        binding.licenceBackUploadBtn.setOnClickListener {
            photoTypeRequest = 2
            openGallery()
        }
    }


    private fun init() {
        toolbarLayoutBinding = binding.documentUploadToolbar
        binding.documentUploadToolbar.toolbarNametxtView.text = getText(R.string.lbl_document)
        binding.documentUploadToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 2)
            getDrivingLicenseDetail()
        }
    }

    private fun updateLincenceDocumnetObserver() {
        if (!uploadDocumentViewModel.updateLicenseDetailData.hasObservers()) {
            uploadDocumentViewModel.updateLicenseDetailData.observe(
                this@DrivingLicenseUploadActivity
            ) { updateLicenseDetailData ->
                updateLicenseDetailData.let {
                    if (updateLicenseDetailData.success) {
                        Toast.makeText(
                            this@DrivingLicenseUploadActivity,
                            getString(R.string.msg_upload_licence_update),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@DrivingLicenseUploadActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        errorData.let {
                            val errorCode = errorData?.errorCode
                            val errorMessage = errorData?.message
                            if (errorCode == 200) {
                                showToast(errorData.message)
                            } else {
                                checkErrorCode(errorCode!!)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun uploadLincenceDocumnetObserver() {
        if (!uploadDocumentViewModel.addLicenseDetailData.hasObservers()) {
            uploadDocumentViewModel.addLicenseDetailData.observe(
                this@DrivingLicenseUploadActivity
            ) { addLicenseDetailData ->
                addLicenseDetailData.let {
                    if (addLicenseDetailData.success) {
                        Toast.makeText(
                            this@DrivingLicenseUploadActivity,
                            getString(R.string.msg_upload_licence),
                            Toast.LENGTH_SHORT
                        ).show()
                        val mIntent = Intent(
                            this@DrivingLicenseUploadActivity,
                            DocumentUploadActivity::class.java
                        )
                        startActivity(mIntent)
                        finish()
                    }
                }
            }
        }
        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@DrivingLicenseUploadActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        errorData.let {
                            val errorCode = errorData?.errorCode
                            val errorMessage = errorData?.message
                            if (errorCode == 200) {
                                showToast(errorData.message)
                            } else {
                                checkErrorCode(errorCode!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getDrivingLicenseDetail() {
        val jsonObject = JSONObject()

        jsonObject.put(Constants.DOCUMENT_TYPE, getDrivingLicenseDetail)

        if (Utils.checkConnection(this@DrivingLicenseUploadActivity)) {
            uploadDocumentViewModel.getLicenceDetailResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@DrivingLicenseUploadActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!uploadDocumentViewModel.getLicenseDetailData.hasObservers()) {
            uploadDocumentViewModel.getLicenseDetailData.observe(
                this@DrivingLicenseUploadActivity
            ) { getLicenseDetailData ->
                getLicenseDetailData.let {
                    if (getLicenseDetailData.success) {
                        binding.licenseDetailSaveBtn.visibility = View.GONE
                        binding.licenseDetailUpdateBtn.visibility = View.VISIBLE
                        getLicenseDetailData.results.records.let { records ->
                            getLicenseDetailData.results.records
                            setLicenseDetail(records)
                        }

                    }
                }
            }
        }
        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@DrivingLicenseUploadActivity)
                        }
                    }
                    LoadingState.Status.SUCCESS -> {
                        Utils.hideProgress()
                    }
                    LoadingState.Status.FAILED -> {
                        Utils.hideProgress()
                        val errorData = loadingState.errorData
                        errorData.let {
                            val errorCode = errorData?.errorCode
                            val errorMessage = errorData?.message
                            if (errorCode == 200) {
                                showToast(errorData.message)
                            } else {
                                checkErrorCode(errorCode!!)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun setLicenseDetail(records: Records) {

        if (records.frontSide.isNotEmpty()) {
            Glide.with(this@DrivingLicenseUploadActivity)
                .load(Constants.PREFIX_DOMAIN.plus(records.frontSide))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.drivingLincenseFrontImgView)
        }
        if (records.backSide.isNotEmpty()) {
            Glide.with(this@DrivingLicenseUploadActivity)
                .load(Constants.PREFIX_DOMAIN.plus(records.backSide))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.drivingLincenseBackImgView)
        }

        val licenceType = records.licenceType

        if (licenceType.equals("Two Wheeler")) {
            binding.licenseTypeSpinner.setSelection(0)
        } else if (licenceType.equals("Light Weight Motor")) {
            binding.licenseTypeSpinner.setSelection(1)
        } else {
            binding.licenseTypeSpinner.setSelection(2)
        }

        if (records.name.isNotEmpty()) {
            binding.userNameEdtView.setText(records.name)
        }
        if (records.number.isNotEmpty()) {
            binding.licenceNumberEdtView.setText(records.number)
        }
        if (records.licenceRegisterDate.isNotEmpty()) {
            binding.registrationDateEdtView.setText(records.licenceRegisterDate)
        }
        if (records.licenceExpiryDate.isNotEmpty()) {
            binding.expiryDateEdtView.setText(records.licenceExpiryDate)
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

            Picker.startPicker(this@DrivingLicenseUploadActivity, mPickerOptions)
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
        if (resultCode == Activity.RESULT_OK && requestCode == Picker.REQUEST_CODE_PICKER) {
            val mImageList =
                data?.getStringArrayListExtra(Picker.PICKED_MEDIA_LIST) as ArrayList //List of selected/captured images/videos
            try {
                CropImage.activity(Uri.parse(mImageList[0])).setAspectRatio(1, 1)
                    .setInitialRotation(0)
                    .setFlipHorizontally(false).setFlipVertically(false)
                    .start(this@DrivingLicenseUploadActivity)
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
//                  binding.userprofileImageView.setImageBitmap(result.bitmap)
                    if (photoTypeRequest == 1) {
                        licenseFrontSideFile = File(result.uri.path)
                        Glide.with(this@DrivingLicenseUploadActivity)
                            .load(imagePath)
                            .error(R.drawable.ic_thumbnail)
                            .placeholder(R.drawable.ic_thumbnail)
                            .into(binding.drivingLincenseFrontImgView)
                    } else if (photoTypeRequest == 2) {
                        licenseBackSideFile = File(result.uri.path)
                        Glide.with(this@DrivingLicenseUploadActivity)
                            .load(imagePath)
                            .error(R.drawable.ic_thumbnail)
                            .placeholder(R.drawable.ic_thumbnail)
                            .into(binding.drivingLincenseBackImgView)
                    }
                    println("onActivityResult: $result")
                    println("onActivityResult: resultUri: $resultUri")
                    println("onActivityResult: imagePath: $imagePath")
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(
        context: Context,
        img: Bitmap,
        selectedImage: Uri
    ): Bitmap? {
        val input: InputStream = context.contentResolver.openInputStream(selectedImage)!!
        val ei: ExifInterface =
            if (Build.VERSION.SDK_INT > 23) ExifInterface(input) else selectedImage.path?.let {
                ExifInterface(it)
            }!!
        return when (ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(img, 270)
            else -> img
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    private fun openDatePickerExpiryDate() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, 0)

        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            binding.expiryDateEdtView.setText(dateFormat.format(calendar.time))
        }
        val datepicker = DatePickerDialog(
            this@DrivingLicenseUploadActivity,
            R.style.datepicker,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datepicker.datePicker.minDate = calendar.timeInMillis
        datepicker.show()
    }

    private fun openDatePickerRegistrationDate() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, 0)

        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)


            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            binding.registrationDateEdtView.setText(dateFormat.format(calendar.time))

        }
        val datepicker = DatePickerDialog(
            this@DrivingLicenseUploadActivity,
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