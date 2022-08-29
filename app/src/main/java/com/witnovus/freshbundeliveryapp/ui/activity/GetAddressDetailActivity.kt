package com.witnovus.freshbundeliveryapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.AddPermanetAddressActivityBinding
import com.witnovus.freshbundeliveryapp.databinding.GetAddressDetailActivityBinding
import com.witnovus.freshbundeliveryapp.databinding.ToolbarLayoutBinding
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GetAddressDetailActivity : BaseActivity() {

    private lateinit var binding: GetAddressDetailActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()

    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_address_detail)
        init()
    }

    private fun init() {
        toolbarLayoutBinding = binding.getAddressToolbar
        binding.getAddressToolbar.toolbarNametxtView.text =
            getText(R.string.lbl_address_detail)
        binding.getAddressToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }

        binding.editCurrentAddressBtn.setOnClickListener {
            val activityIntent =
                Intent(this@GetAddressDetailActivity, UseCurrentAddressActivity::class.java)
            activityIntent.putExtra("type", 3)
            startActivity(activityIntent)
            overridePendingTransition(0, 0)
            finish()
        }
        binding.editPermanentAddressBtn.setOnClickListener {
            val activityIntent =
                Intent(this@GetAddressDetailActivity, UseCurrentAddressActivity::class.java)
            activityIntent.putExtra("type", 4)
            startActivity(activityIntent)
            overridePendingTransition(0, 0)
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        getAddressDetail()
    }
    override fun onStart() {
        super.onStart()
        getAddressDetail()
    }



    private fun getAddressDetail() {
        if (Utils.checkConnection(this@GetAddressDetailActivity)) {
            uploadDocumentViewModel.getAddressDetailResponse(toString())
        } else {
            Toast.makeText(
                this@GetAddressDetailActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }


        if (!uploadDocumentViewModel.getAddressDetailData.hasObservers()) {
            uploadDocumentViewModel.getAddressDetailData.observe(this@GetAddressDetailActivity)
            { getAddressDetailData ->
                getAddressDetailData.let {
                    if (getAddressDetailData.success) {
                        getAddressDetailData.results.records.let { records ->
                            getAddressDetailData.results.records
                            setAddressDetail(records)
                        }
                    }

                    if (!uploadDocumentViewModel.loadingState.hasObservers()) {
                        uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                            when (loadingState.status) {
                                LoadingState.Status.RUNNING -> {
                                    if (!Utils.isProgressShowing()) {
                                        Utils.showProgress(this@GetAddressDetailActivity)
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

        }

    }

    private fun setAddressDetail(records: com.witnovus.freshbundeliveryapp.model.uploaddocument.getaddressdetail.Records) {
        binding.flatNoTxtView.setText(records.currentBuildingName)
        binding.areaTxtView.setText(records.currentArea)
        binding.addressTxtView.setText(records.currentAddress)
        binding.landmarkTxtView.setText(records.currentLandmark)
        binding.stateTxtView.setText(records.currentState)
        binding.cityTxtView.setText(records.currentCity)
        binding.zipCodeTxtView.setText(records.currentZipcode.toString())
        binding.flatNoPermanentAddressTxtView.setText(records.permanentBuildingName)
        binding.areaPermanentAddressTxtView.setText(records.permanentArea)
        binding.permanentAddressTxtView.setText(records.permanentAddress)
        binding.landmarkPermanentAddressTxtView.setText(records.permanentLandmark)
        binding.statePermanentAddressTxtView.setText(records.permanentState)
        binding.cityPermanentAddressTxtView.setText(records.permanentCity)
        binding.zipCodePermanentAddressTxtView.setText(records.permanentZipcode.toString())

    }
}