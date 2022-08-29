package com.witnovus.freshbundeliveryapp.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.AddBankDetailsActivityBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.utils.imagepicker.Picker
import com.witnovus.freshbundeliveryapp.utils.imagepicker.utils.PickerOptions
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.witnovus.nepzep.adapter.calBottom.BankListAdapter
import com.witnovus.nepzep.utils.Event
import com.witnovus.freshbundeliveryapp.databinding.ToolbarLayoutBinding
import com.witnovus.freshbundeliveryapp.model.uploaddocument.addbankdetail.banklist.Record
import com.witnovus.freshbundeliveryapp.model.uploaddocument.getbankdetail.Records
import com.witnovus.freshbundeliveryapp.ui.fragment.BankListDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList

class AddBankDetailsActivity : BaseActivity(), BankListDialogFragment.BankList {


    private lateinit var binding: AddBankDetailsActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()
    lateinit var bankListAdapter: BankListAdapter
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    var fileMultipart: MultipartBody.Part? = null
    private var fileToUploadFullPath: File? = null
    private val PERMISSION_CHECK_CODE_FOR_STORAGE = 1

    private var PERMISSION_CODE: Int = 88808

    private var selectedAccountType = Constants.BLANK_STRING
    var imagePath = ""

    private var selectedBankId: Int = -1
    private var type: Int = 1

    private var selectedBankImage: String = ""
    private var selectedBankName: String = ""
    private var bankList = ArrayList<Record>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bank_details)
        init()

        binding.bankAccountCheckBox.setOnClickListener {
            val checkBox = binding.bankAccountCheckBox.isChecked
            if (!checkBox) {
                binding.addBankDetailLinLayout.visibility = View.GONE
            } else {
                binding.addBankDetailLinLayout.visibility = View.VISIBLE
            }
        }
        val accountTypeArray = resources.getStringArray(R.array.AccountType)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypeArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.accountTypeSpinner.adapter = adapter

        binding.accountTypeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedAccountType = accountTypeArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun init() {

        binding.addBankDetailsUpdateBtn.visibility = View.GONE
        binding.addBankDetailsButton.visibility = View.VISIBLE
        binding.addBankDetailLinLayout.visibility = View.GONE

        toolbarLayoutBinding = binding.bankDetailsToolbar
        binding.bankDetailsToolbar.toolbarNametxtView.text = getText(R.string.title_bank_details)
        binding.bankDetailsToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 1)
        }
        if (type == 2) {
            binding.addBankDetailsUpdateBtn.visibility = View.VISIBLE
            binding.addBankDetailsButton.visibility = View.GONE
            getBankDetail()
        }
        binding.chooseBankLirLayout.setOnClickListener {
            val banklistDialogFragment = BankListDialogFragment()
            banklistDialogFragment.show(
                supportFragmentManager,
                "banklistDialogFragment"
            )
        }

        binding.bankDocumentUploadRltLayout.setOnClickListener {
            openGallery()
        }

        binding.addBankDetailsButton.setOnClickListener {
            val isChecked = binding.bankAccountCheckBox.isChecked
            if (!isChecked) {
                binding.addBankDetailLinLayout.visibility = View.GONE
                val bankFlag = 0
                addBankDetail()
                uploadDocumentViewModel.addBnkDetailResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    bankFlag.toString()
                        .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                    null,
                    null
                )
            } else {
                binding.addBankDetailLinLayout.visibility = View.VISIBLE
                val branchName = binding.branchNameEdtView.text.toString().trim()
                val accountType = selectedAccountType
                val bankAccountNumber = binding.bankAccNoEdtView.text.toString().trim()
                val beneficiaryName = binding.beneficiaryNameEdtView.text.toString().trim()
                val ifscCode = binding.ifscCodeEdtView.text.toString().trim()
                val micrCode = binding.micrCodeEdtView.text.toString().trim()
                val branchCode = binding.branchCodeEdtView.text.toString().trim()
                val bankId = selectedBankId
                val bankFlag = 1
                val checkBox = binding.bankDetailCheckBox.isChecked
                if (bankId == -1) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.msg_select_bank_first),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (bankAccountNumber.isEmpty()) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_bank_account_number),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (beneficiaryName.isEmpty()) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_bank_beneficiary_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (ifscCode.isEmpty()) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_bnak_ifce_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!Utils.isValidVerifyIFSC(ifscCode)) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_this_type_ifce_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (micrCode.isNotEmpty() && micrCode.length != 9) {
                    android.widget.Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_this_type_micr_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (branchCode.isNotEmpty() && branchCode.length != 6) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_enter_branch_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!checkBox) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_select_check_box),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (fileToUploadFullPath != null) {
                    val imgRequestBody =
                        fileToUploadFullPath?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    //fileToUploadFullPath?.asRequestBody("multipart/form-data".toMediaType())
                    imgRequestBody?.let {
                        fileMultipart = MultipartBody.Part.createFormData(
                            "file",
                            fileToUploadFullPath?.name?.trim(),
                            it
                        )
                        if (fileMultipart != null) {
                            addBankDetail()
                            if (Utils.checkConnection(this@AddBankDetailsActivity)) {
                                uploadDocumentViewModel.addBnkDetailResponse(
                                    bankId.toString()
                                        .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    beneficiaryName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    bankAccountNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    accountType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    branchName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    ifscCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    micrCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    branchCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    bankFlag.toString()
                                        .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                    fileMultipart,
                                    imgRequestBody
                                )
                            }
                        } else {
                            Toast.makeText(
                                this@AddBankDetailsActivity,
                                getString(R.string.msg_internet_connection_not_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_Please_upload_bank_Imag),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.addBankDetailsUpdateBtn.setOnClickListener {
            val checkBoxa = binding.bankAccountCheckBox.isChecked
            if (!checkBoxa) {
                binding.addBankDetailLinLayout.visibility = View.GONE
                val bankFlag = 0
                updateBankDetail()
                uploadDocumentViewModel.addBnkDetailResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    bankFlag.toString()
                        .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                    null,
                    null
                )
            } else {
                val branchName = binding.branchNameEdtView.text.toString().trim()
                val accountType = selectedAccountType
                val bankAccountNumber = binding.bankAccNoEdtView.text.toString().trim()
                val beneficiaryName = binding.beneficiaryNameEdtView.text.toString().trim()
                val ifscCode = binding.ifscCodeEdtView.text.toString().trim()
                val micrCode = binding.micrCodeEdtView.text.toString().trim()
                val branchCode = binding.branchCodeEdtView.text.toString().trim()
                val bankId = selectedBankId
                val bankFlag = 1
                val checkBox = binding.bankDetailCheckBox.isChecked
                if (bankId == -1) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.msg_select_bank_first),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (bankAccountNumber.isEmpty()) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_bank_account_number),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (beneficiaryName.isEmpty()) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_bank_beneficiary_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (ifscCode.isEmpty()) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_bnak_ifce_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!Utils.isValidVerifyIFSC(ifscCode)) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_this_type_ifce_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (micrCode.isNotEmpty() && micrCode.length != 9) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_enter_this_type_micr_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (branchCode.isNotEmpty() && branchCode.length != 6) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_enter_branch_code),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!checkBox) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.lbl_please_select_check_box),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (fileToUploadFullPath == null) {
                    updateBankDetail()
                    if (Utils.checkConnection(this@AddBankDetailsActivity)) {
                        uploadDocumentViewModel.addBnkDetailResponse(
                            bankId.toString()
                                .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            beneficiaryName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            bankAccountNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            accountType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            branchName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            ifscCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            micrCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            branchCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            bankFlag.toString()
                                .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                            null,
                            null
                        )
                    } else {
                        Toast.makeText(
                            this@AddBankDetailsActivity,
                            getString(R.string.msg_internet_connection_not_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val imgRequestBody =
                        fileToUploadFullPath?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    imgRequestBody?.let {
                        fileMultipart = MultipartBody.Part.createFormData(
                            "file",
                            fileToUploadFullPath?.name!!.trim(),
                            it
                        )
                    }
                    if (fileMultipart != null) {
                        updateBankDetail()
                        if (Utils.checkConnection(this@AddBankDetailsActivity)) {
                            uploadDocumentViewModel.addBnkDetailResponse(
                                bankId.toString()
                                    .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                beneficiaryName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                bankAccountNumber.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                accountType.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                branchName.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                ifscCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                micrCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                branchCode.toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                bankFlag.toString()
                                    .toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull()),
                                fileMultipart,
                                imgRequestBody
                            )
                        }
                    } else {
                        Toast.makeText(
                            this@AddBankDetailsActivity,
                            getString(R.string.msg_internet_connection_not_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun getBankDetail() {

        if (Utils.checkConnection(this@AddBankDetailsActivity)) {
            uploadDocumentViewModel.getBankDetailResponse(toString())
        } else {
            Toast.makeText(
                this@AddBankDetailsActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }


        if (!uploadDocumentViewModel.getBankDetailData.hasObservers()) {
            uploadDocumentViewModel.getBankDetailData.observe(
                this@AddBankDetailsActivity
            ) { getBankDetailData ->
                getBankDetailData.let {
                    if (getBankDetailData.success) {

                        binding.addBankDetailsButton.visibility = View.GONE
                        binding.addBankDetailsUpdateBtn.visibility = View.VISIBLE
                        getBankDetailData.results.records.let { records ->
                            getBankDetailData.results.records
                            setBankDetailRecord(records)
                            binding.bankAccountCheckBox.isChecked = true
                            binding.addBankDetailsUpdateBtn.visibility = View.VISIBLE
                            binding.addBankDetailsButton.visibility = View.GONE
                            binding.addBankDetailLinLayout.visibility = View.VISIBLE
                        }
                    } else {
                        binding.bankAccountCheckBox.isChecked = false
                        binding.addBankDetailsUpdateBtn.visibility = View.GONE
                        binding.addBankDetailsButton.visibility = View.VISIBLE
                        binding.addBankDetailLinLayout.visibility = View.GONE
                    }
                }
            }
        }

        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@AddBankDetailsActivity)
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

    private fun setBankDetailRecord(records: Records) {

        selectedBankId = records.bank.id
        selectedBankName = records.bank.bankName
        selectedBankImage = records.bank.bankLogo
        //binding.bankTitleTxtView.setText(records.bankId.toString())
        val bankImgage = Constants.BASE_URL_IMAGE + selectedBankImage
        Glide.with(this)
            .load(bankImgage)
            .error(R.drawable.ic_thumbnail)
            .placeholder(R.drawable.ic_thumbnail)
            .into(binding.bankLogoView)

        binding.bankTitleTxtView.setText(selectedBankName)

        if (records.file.isNotEmpty()) {
            Glide.with(this@AddBankDetailsActivity)
                .load(Constants.PREFIX_DOMAIN.plus(records.file))
                .error(R.drawable.ic_thumbnail)
                .placeholder(R.drawable.ic_thumbnail)
                .into(binding.bankDetailImgView)

            binding.bankDocumentUploadImgView.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_added_document_icon
                )
            )

        }



        Glide.with(this@AddBankDetailsActivity)
            .load(Constants.BASE_URL_IMAGE + records.file)
            .error(R.drawable.ic_thumbnail)
            .placeholder(R.drawable.ic_thumbnail)
            .into(binding.bankDetailImgView)



        val accountType = records.accountType

        if (accountType.equals("Current")){
            binding.accountTypeSpinner.setSelection(0)
        }else{
            binding.accountTypeSpinner.setSelection(1)
        }

        if (records.branchName.isNotEmpty()) {
            binding.branchNameEdtView.setText(records.branchName)
        }
        if (records.accountNo.isNotEmpty()) {
            binding.bankAccNoEdtView.setText(records.accountNo)
        }
        if (records.accountName.isNotEmpty()) {
            binding.beneficiaryNameEdtView.setText(records.accountName)
        }
        if (records.ifscCode.isNotEmpty()) {
            binding.ifscCodeEdtView.setText(records.ifscCode)
        }
        binding.micrCodeEdtView.setText(records.micrCode)
        binding.branchCodeEdtView.setText(records.branchCode)

    }

    private fun addBankDetail() {

        if (!uploadDocumentViewModel.addBankDetailData.hasObservers()) {
            uploadDocumentViewModel.addBankDetailData.observe(
                this@AddBankDetailsActivity
            ) { addBankDetailData ->
                addBankDetailData.let {
                    if (addBankDetailData.success) {
                        if (addBankDetailData.results?.records != null) {
                            Toast.makeText(
                                this@AddBankDetailsActivity,
                                getString(R.string.msg_add_bank),
                                Toast.LENGTH_SHORT
                            ).show()
                            val activityIntent = Intent(
                                    this@AddBankDetailsActivity,
                                    UnderReviewProfileActivity::class.java
                                )
                                activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                startActivity(activityIntent)
                                overridePendingTransition(0, 0)
                                finish()
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
                            Utils.showProgress(this@AddBankDetailsActivity)
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
                                    this@AddBankDetailsActivity,
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

    private fun updateBankDetail() {
        if (!uploadDocumentViewModel.addBankDetailData.hasObservers()) {
            uploadDocumentViewModel.addBankDetailData.observe(
                this@AddBankDetailsActivity
            ) { addBankDetailData ->
                if (addBankDetailData.success) {
                    Toast.makeText(
                        this@AddBankDetailsActivity,
                        getString(R.string.msg_update_bank),
                        Toast.LENGTH_SHORT
                    ).show()
                    val activityIntent = Intent(
                                this@AddBankDetailsActivity,
                                MainActivity::class.java
                            )
                            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            startActivity(activityIntent)
                            overridePendingTransition(0, 0)
                            finish()

                    }

            }
        }

        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@AddBankDetailsActivity)
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


    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissionsList = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permissionsList, PERMISSION_CHECK_CODE_FOR_STORAGE)
        } else {
            openPhotoPicker()
        }
    }

    private fun openPhotoPicker() {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
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

            Picker.startPicker(this@AddBankDetailsActivity, mPickerOptions)
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
                    .start(this@AddBankDetailsActivity)
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
                    fileToUploadFullPath = File(result.uri.path)
                    Glide.with(this@AddBankDetailsActivity)
                        .load(imagePath)
                        .error(R.drawable.ic_thumbnail)
                        .placeholder(R.drawable.ic_thumbnail)
                        .into(binding.bankDetailImgView)
                    println("onActivityResult: $result")
                    println("onActivityResult: resultUri: $resultUri")
                    println("onActivityResult: imagePath: $imagePath")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        var isBankAdded: MutableLiveData<Event<Boolean>> =
            MutableLiveData<Event<Boolean>>()

    }

    override fun onSelect(bank: Record?) {
        selectedBankId = bank!!.id
        selectedBankImage = bank.bankLogo!!
        selectedBankName = bank.bankName!!

        val bankImgage = Constants.BASE_URL_IMAGE + selectedBankImage
        Glide.with(this)
            .load(bankImgage)
            .error(R.drawable.ic_thumbnail)
            .placeholder(R.drawable.ic_thumbnail)
            .into(binding.bankLogoView)

        binding.bankTitleTxtView.setText(selectedBankName)
        binding.branchNameEdtView.text = null
        binding.bankAccNoEdtView.text = null
        binding.beneficiaryNameEdtView.text = null
        binding.ifscCodeEdtView.text = null
        binding.micrCodeEdtView.text = null
        binding.branchCodeEdtView.text = null

        Glide.with(this@AddBankDetailsActivity)
            .load("")
            .error(R.drawable.ic_thumbnail)
            .placeholder(R.drawable.ic_thumbnail)
            .into(binding.bankDetailImgView)
    }
}