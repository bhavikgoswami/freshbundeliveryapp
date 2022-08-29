package com.witnovus.freshbundeliveryapp.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.AadhaarCardUploadActivityBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.utils.imagepicker.Picker
import com.witnovus.freshbundeliveryapp.utils.imagepicker.utils.PickerOptions
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.witnovus.freshbundeliveryapp.databinding.ToolbarLayoutBinding
import com.witnovus.freshbundeliveryapp.model.uploaddocument.getadharcarddetail.Records
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.util.ArrayList

class AadharCardUploadActivity : BaseActivity() {

    private lateinit var binding: AadhaarCardUploadActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding


    private var aadhaarCardFrontMultipart: MultipartBody.Part? = null
    private var aadhaarCardFrontSideFile: File? = null
    private var aadhaarCardBackMultipart: MultipartBody.Part? = null
    private var aadhaarCardBackSideFile: File? = null

    private val PERMISSION_CHECK_CODE_FOR_STORAGE = 1
    private var photoTypeRequest: Int = 1
    var imagePath = Constants.BLANK_STRING
    private var type: Int = 1

    private var getAdharCardDetail = Constants.DOCUMENT_TYPE_AADHAAR_CARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_aadhar_card_upload)

        binding.aadhaarCardDetailUpdateBtn.visibility = View.GONE
        binding.aadhaarCardDetailSaveBtn.visibility = View.VISIBLE

        init()



        open class AdhaarNumberTextWatcher(private val editText: EditText) : TextWatcher {
            private val isDelete = false
            private val lastContentLength = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                //The requirement is 130 1234 4567, the fourth and fifth digit spaces in the middle are preceded by spaces
                val sb = StringBuffer(s)
                //StringBuffer.length() is the length, so the subscript starts from 1
                //If it is not a space character, insert a space character in front of it
                if (s.length >= 5) {
                    val chars = s.toString().toCharArray()
                    //The number subscript starts at 0
                    if (chars[4] != ' ') {
                        sb.insert(4, ' ')
                        setContent(sb)
                    }
                }
                if (s.length >= 10) {
                    val chars = s.toString().toCharArray()
                    //Because a space is added to the fourth digit, the eighth digit is the ninth digit of the character array, and the subscript is 8.
                    if (chars[9] != ' ') {
                        sb.insert(9, ' ')
                        setContent(sb)
                    }
                }
            }

            /**
             * Add or remove space EditText settings
             */
            private fun setContent(sb: StringBuffer) {
                editText.setText(sb.toString())
                //Move the cursor to the back
                editText.setSelection(sb.length)
            }
        }

        binding.aadhaarCardNumberEdtView.addTextChangedListener(object :
            AdhaarNumberTextWatcher(binding.aadhaarCardNumberEdtView) {
            override fun afterTextChanged(s: Editable) {
                super.afterTextChanged(s)

            }
        })

        binding.aadhaarCardDetailSaveBtn.setOnClickListener {

            val documentType = Constants.DOCUMENT_TYPE_AADHAAR_CARD
            val aadhaarCardUserName =
                binding.aadhaarCardUserNameEdtView.text.toString().trim()
            val aadhaarCardNumber = binding.aadhaarCardNumberEdtView.text.toString().trim()

            if (aadhaarCardUserName.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_aadhaarcardusername))
            } else if (aadhaarCardNumber.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_aadhaarcardNumber))
            } else if (!Utils.isValidAadhaarCardNumber(aadhaarCardNumber)) {
                showToast(getString(R.string.lbl_Please_enter_aadhaarcardNumber_validation))
            } else {
                uploadAadhaarCardDocumentObserver()
                if (Utils.checkConnection(this@AadharCardUploadActivity)) {
                    if (aadhaarCardFrontSideFile != null) {
                        val frontSiderequestBody =
                            aadhaarCardFrontSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        frontSiderequestBody?.let {
                            aadhaarCardFrontMultipart = MultipartBody.Part.createFormData(
                                "front_side",
                                aadhaarCardFrontSideFile?.name,
                                it
                            )
                        }
                        if (aadhaarCardBackSideFile != null) {
                            val backSiderequestBody =
                                aadhaarCardBackSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            backSiderequestBody?.let {
                                aadhaarCardBackMultipart =
                                    MultipartBody.Part.createFormData(
                                        "back_side",
                                        aadhaarCardBackSideFile?.name,
                                        it
                                    )
                            }
                            if (frontSiderequestBody != null && backSiderequestBody != null) {
                                uploadDocumentViewModel.addAadhaarCardDetailResponse(
                                    documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    aadhaarCardUserName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    aadhaarCardNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    aadhaarCardFrontMultipart,
                                    frontSiderequestBody,
                                    aadhaarCardBackMultipart,
                                    backSiderequestBody
                                )
                            }
                        }
                    } else {
                        showToast(getString(R.string.lbl_Please_upload_aadhaarcard_Imag))
                    }
                } else {
                    Toast.makeText(
                        this@AadharCardUploadActivity,
                        getString(R.string.msg_internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.aadhaarCardDetailUpdateBtn.setOnClickListener {
            val documentType = Constants.DOCUMENT_TYPE_AADHAAR_CARD
            val aadhaarCardUserName = binding.aadhaarCardUserNameEdtView.text.toString().trim()
            val aadhaarCardNumber = binding.aadhaarCardNumberEdtView.text.toString().trim()

            if (aadhaarCardUserName.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_aadhaarcardusername))
            } else if (aadhaarCardNumber.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_aadhaarcardNumber))
            } else if (!Utils.isValidAadhaarCardNumber(aadhaarCardNumber)) {
                showToast(getString(R.string.lbl_Please_enter_aadhaarcardNumber_validation))
            } else {
                updateAadhaarCardDocumentObserver()
                if (Utils.checkConnection(this@AadharCardUploadActivity)) {
                    if (aadhaarCardFrontSideFile == null && aadhaarCardBackSideFile == null) {
                        uploadDocumentViewModel.updateAadhaarCardDetailResponse(
                            documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            aadhaarCardUserName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            aadhaarCardNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            null,
                            null,
                            null,
                            null
                        )
                    } else {
                        val frontSiderequestBody =
                            aadhaarCardFrontSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        frontSiderequestBody?.let {
                            aadhaarCardFrontMultipart = MultipartBody.Part.createFormData(
                                "front_side",
                                aadhaarCardFrontSideFile?.name,
                                it
                            )
                        }

                        val backSiderequestBody =
                            aadhaarCardBackSideFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        backSiderequestBody?.let {
                            aadhaarCardBackMultipart = MultipartBody.Part.createFormData(
                                "back_side",
                                aadhaarCardBackSideFile?.name,
                                it
                            )
                        }
                        uploadDocumentViewModel.updateAadhaarCardDetailResponse(
                            documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            aadhaarCardUserName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            aadhaarCardNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            aadhaarCardFrontMultipart,
                            frontSiderequestBody,
                            aadhaarCardBackMultipart,
                            backSiderequestBody
                        )
                    }

                } else {
                    Toast.makeText(
                        this@AadharCardUploadActivity,
                        getString(R.string.msg_internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        binding.aadhaarCardFrontImgUploadBtn.setOnClickListener {
            photoTypeRequest = 1
            openGallery()
        }
        binding.aadhaarCardBackImgUploadBtn.setOnClickListener {
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
            getAdharCardDetail()
        }

    }

    private fun updateAadhaarCardDocumentObserver() {
        if (!uploadDocumentViewModel.updateAadhaarCardDetailData.hasObservers()) {
            uploadDocumentViewModel.updateAadhaarCardDetailData.observe(
                this@AadharCardUploadActivity
            ) { updateAadhaarCardDetailData ->
                updateAadhaarCardDetailData.let {
                    if (updateAadhaarCardDetailData.success) {
                        Toast.makeText(
                            this@AadharCardUploadActivity,
                            getString(R.string.msg_upload_aadhaarcard_update),
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
                            Utils.showProgress(this@AadharCardUploadActivity)
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


    private fun uploadAadhaarCardDocumentObserver() {
        if (!uploadDocumentViewModel.addAadhaarCardDetailData.hasObservers()) {
            uploadDocumentViewModel.addAadhaarCardDetailData.observe(
                this@AadharCardUploadActivity
            ) { addAadhaarCardDetailData ->
                addAadhaarCardDetailData.let {
                    if (addAadhaarCardDetailData.success) {
                        Toast.makeText(
                            this@AadharCardUploadActivity,
                            getString(R.string.msg_upload_aadhaarcard_update),
                            Toast.LENGTH_SHORT
                        ).show()
                        val mIntent = Intent(
                            this@AadharCardUploadActivity,
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
                            Utils.showProgress(this@AadharCardUploadActivity)
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
                            if (errorMessage != null) {
                                Toast.makeText(
                                    this@AadharCardUploadActivity,
                                    errorData.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getAdharCardDetail() {
        val jsonObject = JSONObject()
        jsonObject.put(Constants.DOCUMENT_TYPE, getAdharCardDetail)

        if (Utils.checkConnection(this@AadharCardUploadActivity)) {
            uploadDocumentViewModel.getAdharCardDetailResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@AadharCardUploadActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!uploadDocumentViewModel.getAadhaarCardDetailData.hasObservers()) {
            uploadDocumentViewModel.getAadhaarCardDetailData.observe(
                this@AadharCardUploadActivity
            ) { getAadhaarCardDetailData ->
                getAadhaarCardDetailData.let {
                    if (getAadhaarCardDetailData.success) {
                        binding.aadhaarCardDetailUpdateBtn.visibility = View.VISIBLE
                        binding.aadhaarCardDetailSaveBtn.visibility = View.GONE
                        getAadhaarCardDetailData.results.records.let { records ->
                            getAadhaarCardDetailData.results.records
                            setAdharCardDetail(records)
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
                            Utils.showProgress(this@AadharCardUploadActivity)
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
                            if (errorMessage != null) {
                                Toast.makeText(
                                    this@AadharCardUploadActivity,
                                    errorData.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun setAdharCardDetail(records: Records) {
        if (records.frontSide.isNotEmpty()) {
            Glide.with(this@AadharCardUploadActivity)
                .load(Constants.PREFIX_DOMAIN.plus(records.frontSide))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.aadhaarCardFrontImgView)
        }
        if (records.backSide.isNotEmpty()) {
            Glide.with(this@AadharCardUploadActivity)
                .load(Constants.PREFIX_DOMAIN.plus(records.backSide))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.aadhaarCardBackImgView)
        }

        if (records.name.isNotEmpty()) {
            binding.aadhaarCardUserNameEdtView.setText(records.name)
        }
        if (records.number.isNotEmpty()) {
            binding.aadhaarCardNumberEdtView.setText(records.number)
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

            Picker.startPicker(this@AadharCardUploadActivity, mPickerOptions)
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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                    .start(this@AadharCardUploadActivity)
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
                        aadhaarCardFrontSideFile = File(result.uri.path)
                        Glide.with(this@AadharCardUploadActivity)
                            .load(imagePath)
                            .error(R.drawable.ic_thumbnail)
                            .placeholder(R.drawable.ic_thumbnail)
                            .into(binding.aadhaarCardFrontImgView)
                    } else if (photoTypeRequest == 2) {
                        aadhaarCardBackSideFile = File(result.uri.path)
                        Glide.with(this@AadharCardUploadActivity)
                            .load(imagePath)
                            .error(R.drawable.ic_thumbnail)
                            .placeholder(R.drawable.ic_thumbnail)
                            .into(binding.aadhaarCardBackImgView)
                    }
                    println("onActivityResult: $result")
                    println("onActivityResult: resultUri: $resultUri")
                    println("onActivityResult: imagePath: $imagePath")
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Utils.isProgressShowing()) {
            Utils.hideProgress()
        }
    }
}