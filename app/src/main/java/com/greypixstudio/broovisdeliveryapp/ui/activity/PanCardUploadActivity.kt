package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.PanCardUploadActivityBinding
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.Picker
import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.utils.PickerOptions
import com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import com.theartofdev.edmodo.cropper.CropImage
import io.paperdb.Paper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.util.ArrayList

class PanCardUploadActivity : AppCompatActivity() {
/*

    private lateinit var binding: PanCardUploadActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()

    private var panCardMultipart: MultipartBody.Part? = null
    private var panCardFile: File? = null

    private var PERMISSION_CODE: Int = 88808
    private var imagePath = Constants.BLANK_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pan_card_upload)

        binding.panCardImageUploadBtn.setOnClickListener {
            openPhotoPicker()
        }
        binding.panCardDetailSaveBtn.setOnClickListener {
            val documentType = Constants.DOCUMENT_TYPE_PAN_CARD
            val panCardUserName = binding.panCardNameEdtView.text.toString().trim()
            val panCardNumber = binding.panCardNumberEdtView.text.toString().trim()

             if (panCardUserName.isEmpty()) {
                 showToast(getString(R.string.lbl_Please_enter_pancardusername))
             } else if (panCardNumber.isEmpty()) {
                 showToast(getString(R.string.lbl_Please_enter_pancardNumber))
             }else if(!Utils.isValidPanrCardNumber(panCardNumber)){
                 showToast(getString(R.string.lbl_Please_enter_pancardNumber_validation))
             } else {
                uploadPanCardDocumentObserver()
                if (panCardFile != null) {
                    val requestBody =
                        panCardFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    requestBody?.let {
                        panCardMultipart = MultipartBody.Part.createFormData(
                            "front_side",
                            panCardFile?.name,
                            it
                        )
                    }

                    if (requestBody != null) {
                        uploadDocumentViewModel.addPanCardDetailResponse(
                            documentType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            panCardUserName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            panCardNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            panCardMultipart,
                            requestBody
                        )
                    }

                } else {
                    showToast(getString(R.string.lbl_Please_upload_pancard_Imag))
                }
            }

        }
    }
    private fun showToast(msg: String) {
        Toast.makeText(this@PanCardUploadActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun uploadPanCardDocumentObserver() {
        if (!uploadDocumentViewModel.addPanCardDetailData.hasObservers()) {
            uploadDocumentViewModel.addPanCardDetailData.observe(this@PanCardUploadActivity,
                { addPanCardDetailData ->
                    addPanCardDetailData.let {
                        if (addPanCardDetailData.success) {

                            Paper.book().write(Constants.UPLOAD_LICENSE, addPanCardDetailData.results.records.status)

                            val mIntent = Intent(this@PanCardUploadActivity, DocumentUploadActivity::class.java)
                            startActivity(mIntent)
                            finish()
                        }
                    }
                })
        }
        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this, { loadingState ->
                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@PanCardUploadActivity)
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
                                    this@PanCardUploadActivity,
                                    errorData.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            })
        }

    }



    private fun openPhotoPicker() {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,

            )
        val pm = applicationContext.packageManager
        var hasPermisson = false
        permissions.forEach {
            val hasPerm = pm.checkPermission(it, packageName)
            hasPermisson = hasPerm == PackageManager.PERMISSION_GRANTED
        }

        if (!hasPermisson) {
            requestPermissions(permissions, PERMISSION_CODE)
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

            Picker.startPicker(this@PanCardUploadActivity, mPickerOptions)
        } catch (e: Exception) {
            e.printStackTrace()
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
                    .start(this@PanCardUploadActivity)
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
                    panCardFile = File(result.uri.path)
                    Glide.with(this@PanCardUploadActivity)
                        .load(imagePath)
                        .error(R.drawable.amul_logo)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(binding.panCardImgView)
                    println("onActivityResult: $result")
                    println("onActivityResult: resultUri: $resultUri")
                    println("onActivityResult: imagePath: $imagePath")
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
*/


}