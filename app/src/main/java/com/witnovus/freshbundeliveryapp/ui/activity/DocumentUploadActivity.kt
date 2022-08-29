package com.witnovus.freshbundeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.DocumentUploadActivityBinding
import com.witnovus.freshbundeliveryapp.databinding.ToolbarLayoutBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import kotlinx.android.synthetic.main.activity_document_upload.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DocumentUploadActivity : BaseActivity() {

    private lateinit var binding: DocumentUploadActivityBinding

    private var uploadDocumentLicence: Boolean = false
    private var uploadDocumentPancard: Boolean = false
    private var uploadDocumentAadharCard: Boolean = false

    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    private var type: Int = 1

    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_document_upload)
        init()
        binding.apply {
            drivingLicenseDocumentAddedImgView.visibility = View.GONE
            drivingLicenseDocumentUploadImgView.visibility = View.VISIBLE
            aadhaarCardUploadImgView.visibility = View.VISIBLE
            aadhaarCardAddedImgView.visibility = View.GONE
        }

        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 1)

        }
        if(type == 2){
            binding.uploadDocumentCheckBox.visibility = View.GONE
            binding.uploadDocumentButton.visibility = View.GONE

        }
        if (type == 1) {
            binding.drivingLicenseRltLayout.setOnClickListener {
                val mIntent =
                    Intent(this@DocumentUploadActivity, DrivingLicenseUploadActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(mIntent)
                overridePendingTransition(0, 0)
                finish()
            }
        } else {
            binding.drivingLicenseRltLayout.setOnClickListener {
                val mIntent =
                    Intent(this@DocumentUploadActivity, DrivingLicenseUploadActivity::class.java)
                mIntent.putExtra("type", 2)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(mIntent)
                overridePendingTransition(0, 0)
                finish()
            }
        }

        if (type == 1) {
            binding.aadharCardRltLayout.setOnClickListener {
                val mIntent =
                    Intent(this@DocumentUploadActivity, AadharCardUploadActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(mIntent)
                overridePendingTransition(0, 0)
                finish()
            }
        } else {
            binding.aadharCardRltLayout.setOnClickListener {
                val mIntent =
                    Intent(this@DocumentUploadActivity, AadharCardUploadActivity::class.java)
                mIntent.putExtra("type", 2)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(mIntent)
                overridePendingTransition(0, 0)
                finish()
            }

        }

        binding.uploadDocumentButton.setOnClickListener {
            val checkBox = binding.uploadDocumentCheckBox.isChecked
            val drivingLicense = uploadDocumentLicence
            val aadhaarCard = uploadDocumentAadharCard
            val panCard = uploadDocumentPancard

            if (type == 1){
                if (!drivingLicense) {
                    Toast.makeText(
                        this@DocumentUploadActivity,
                        getString(R.string.lbl_please_upload_your_license),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.drivingLicenseDocumentUploadImgView.visibility = View.VISIBLE
                    binding.drivingLicenseDocumentAddedImgView.visibility = View.GONE
                } else if (!aadhaarCard) {
                    Toast.makeText(
                        this@DocumentUploadActivity,
                        getString(R.string.lbl_please_upload_your_aadharcard),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.aadhaarCardUploadImgView.visibility = View.VISIBLE
                    binding.aadhaarCardAddedImgView.visibility = View.GONE
                } else if (!checkBox) {
                    Toast.makeText(
                        this@DocumentUploadActivity,
                        getString(R.string.lbl_please_select_check_box),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (type == 1) {
                    val activityIntent = Intent(
                        this@DocumentUploadActivity,
                        EnableLocationActivity::class.java
                    )
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(activityIntent)
                    overridePendingTransition(0, 0)
                    finish()

                }
            }

            if(type == 2){
                if (!checkBox) {
                    Toast.makeText(
                        this@DocumentUploadActivity,
                        getString(R.string.lbl_please_select_check_box),
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    val activityIntent = Intent(
                        this@DocumentUploadActivity,
                        MainActivity::class.java
                    )
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(activityIntent)
                    overridePendingTransition(0, 0)
                    finish()
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        documentListObserver()
    }

    private fun init() {
        toolbarLayoutBinding = binding.documentUploadToolbar
        binding.documentUploadToolbar.toolbarNametxtView.text = getText(R.string.lbl_document)
        binding.documentUploadToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
    }

    private fun documentListObserver() {
        if (Utils.checkConnection(this@DocumentUploadActivity)) {
            uploadDocumentViewModel.documentListDetailResponse(toString())
        } else {
            Toast.makeText(
                this@DocumentUploadActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!uploadDocumentViewModel.documentListDetailData.hasObservers()) {
            uploadDocumentViewModel.documentListDetailData.observe(this@DocumentUploadActivity)
            { documentListDetailData ->
                documentListDetailData.let {
                    if (documentListDetailData.success) {
                        if (documentListDetailData.results.records.licence.equals(Constants.BLANK_STRING)) {
                            binding.drivingLicenseDocumentAddedImgView.visibility = View.GONE
                            binding.drivingLicenseDocumentUploadImgView.visibility = View.VISIBLE
                        }
                        if (documentListDetailData.results.records.licence.equals(Constants.STATUS_PENDING)) {
                            binding.drivingLicenseDocumentAddedImgView.visibility = View.VISIBLE
                            binding.drivingLicenseDocumentUploadImgView.visibility = View.GONE
                        }
                        if (documentListDetailData.results.records.aadhaarCard.equals(Constants.BLANK_STRING)) {
                            binding.aadhaarCardUploadImgView.visibility = View.VISIBLE
                            binding.aadhaarCardAddedImgView.visibility = View.GONE
                        }
                        if (documentListDetailData.results.records.aadhaarCard.equals(Constants.STATUS_PENDING)) {
                            binding.aadhaarCardUploadImgView.visibility = View.GONE
                            binding.aadhaarCardAddedImgView.visibility = View.VISIBLE
                        }

                        if (documentListDetailData.results.records.licence.equals(Constants.STATUS_APPROVE)) {
                            binding.drivingLicenseDocumentUploadImgView.visibility = View.GONE
                            binding.drivingLicenseDocumentAddedImgView.visibility = View.VISIBLE
                        }

                        if (documentListDetailData.results.records.aadhaarCard.equals(Constants.STATUS_APPROVE)) {
                            binding.aadhaarCardUploadImgView.visibility = View.GONE
                            binding.aadhaarCardAddedImgView.visibility = View.VISIBLE
                        }

                        uploadDocumentLicence =
                            documentListDetailData.results.records.licence.equals(Constants.STATUS_PENDING)

                        uploadDocumentAadharCard =
                            documentListDetailData.results.records.aadhaarCard.equals(Constants.STATUS_PENDING)

                        uploadDocumentPancard =
                            documentListDetailData.results.records.panCard.equals(Constants.STATUS_PENDING)


                    }
                }
            }
        }

        if (!uploadDocumentViewModel.loadingState.hasObservers()) {
            uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                when (loadingState.status) {
                    LoadingState.Status.RUNNING -> {
                        if (!Utils.isProgressShowing()) {
                            Utils.showProgress(this@DocumentUploadActivity)
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
}