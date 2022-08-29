package com.witnovus.freshbundeliveryapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.AddPermanetAddressActivityBinding
import com.witnovus.freshbundeliveryapp.databinding.ToolbarLayoutBinding
import com.witnovus.freshbundeliveryapp.model.JsonConstants
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.model.uploaddocument.AddressData
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Constants
import com.witnovus.freshbundeliveryapp.utils.Utils
import com.witnovus.freshbundeliveryapp.utils.Utils.Companion.showToast
import com.witnovus.freshbundeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import io.paperdb.Paper
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPermanetAddressActivity : BaseActivity() {

    private lateinit var binding: AddPermanetAddressActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()

    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding

    private var latitude = 0.0
    private var longitude = 0.0

    private var permanentlatitude = 0.0
    private var permanentlongitude = 0.0

    private var flatNo = ""
    private var area = ""
    private var address = ""
    private var landmark = ""
    private var state = ""
    private var city = ""
    private var zipCode = ""

    private var type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_permanet_address)
        init()
        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 1)
            if (type == 2) {
                binding.saveAddAddressUpdateBtn.visibility = View.GONE
                binding.saveAddAddressDetailBtn.visibility = View.VISIBLE
            }
            if (type == 4){
                binding.saveAddAddressUpdateBtn.visibility = View.VISIBLE
                binding.saveAddAddressDetailBtn.visibility = View.GONE
            }
        }


        if (Paper.book().contains(Constants.PERMANENT_ADDRESS_DATA)) {
            binding.saveAddAddressDetailBtn.visibility = View.VISIBLE
            val permanentAddressData =
                Paper.book().read<AddressData>(Constants.PERMANENT_ADDRESS_DATA)
            val flatNo = permanentAddressData.currentBuildingName
            val area = permanentAddressData.currentArea
            val address = permanentAddressData.currentAddress
            val landmark = permanentAddressData.currentLandmark
            permanentlatitude = permanentAddressData.currentLatitudes
            permanentlongitude = permanentAddressData.currentLongitudes
            val state = permanentAddressData.currentState
            val city = permanentAddressData.currentCity
            val zipCode = permanentAddressData.currentZipcode

            binding.permanentAddressFlatNoEdtView.setText(flatNo)
            binding.permanentAddressAreaEdtView.setText(area)
            binding.permanentAddressEdtView.setText(address)
            binding.permanentAddressLandmarkEdtView.setText(landmark)
            binding.cityPermanentAddressEdtView.setText(city)
            binding.statePermanentAddressEdtView.setText(state)
            binding.zipCodePermanentAddressEdtView.setText(zipCode)
        }
    }

    private fun init() {


        toolbarLayoutBinding = binding.addPermanetAddressToolbar
        binding.addPermanetAddressToolbar.toolbarNametxtView.text =
            getText(R.string.lbl_add_address)
        binding.addPermanetAddressToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }
        binding.saveAddAddressDetailBtn.setOnClickListener {
            if (Paper.book().contains(Constants.CURRENT_ADDRESS_DATA)) {
                val currentAddressData =
                    Paper.book().read<AddressData>(Constants.CURRENT_ADDRESS_DATA)
                flatNo = currentAddressData.currentBuildingName
                area = currentAddressData.currentArea
                address = currentAddressData.currentAddress
                landmark = currentAddressData.currentLandmark
                latitude = currentAddressData.currentLatitudes
                longitude = currentAddressData.currentLongitudes
                state = currentAddressData.currentState
                city = currentAddressData.currentCity
                zipCode = currentAddressData.currentZipcode
            }
            val permanentFlatNo = binding.permanentAddressFlatNoEdtView.text.toString()
           val permanentAreaAddress = binding.permanentAddressAreaEdtView.text.toString()
           val permanentAddress = binding.permanentAddressEdtView.text.toString()
           val permanentLandmark = binding.permanentAddressLandmarkEdtView.text.toString()
           val cityPermanentAddress = binding.cityPermanentAddressEdtView.text.toString()
           val statePermanentAddress = binding.statePermanentAddressEdtView.text.toString()
           val zipCodePermanentAddress = binding.zipCodePermanentAddressEdtView.text.toString()
            if (permanentFlatNo.isEmpty()) {
                showToast(getString(R.string.lbl_Please_flat_no))
            } else if (permanentAreaAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_area))
            } else if (permanentAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_address))
            } else if (permanentLandmark.isEmpty()) {
                showToast(getString(R.string.lbl_Please_landmark))
            } else if (statePermanentAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_state))
            } else if (cityPermanentAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_city))
            } else if (zipCodePermanentAddress.isEmpty() == true) {
                showToast(getString(R.string.lbl_Please_enter_zipcode))
            } else {
                addAddressDetailObserver()
            }

        }

        binding.saveAddAddressUpdateBtn.setOnClickListener {
            if (Paper.book().contains(Constants.CURRENT_ADDRESS_DATA)) {
                val currentAddressData =
                    Paper.book().read<AddressData>(Constants.CURRENT_ADDRESS_DATA)
                flatNo = currentAddressData.currentBuildingName
                area = currentAddressData.currentArea
                address = currentAddressData.currentAddress
                landmark = currentAddressData.currentLandmark
                latitude = currentAddressData.currentLatitudes
                longitude = currentAddressData.currentLongitudes
                state = currentAddressData.currentState
                city = currentAddressData.currentCity
                zipCode = currentAddressData.currentZipcode
            }
            val permanentFlatNo = binding.permanentAddressFlatNoEdtView.text.toString()
            val permanentAreaAddress = binding.permanentAddressAreaEdtView.text.toString()
            val permanentAddress = binding.permanentAddressEdtView.text.toString()
            val permanentLandmark = binding.permanentAddressLandmarkEdtView.text.toString()
            val cityPermanentAddress = binding.cityPermanentAddressEdtView.text.toString()
            val statePermanentAddress = binding.statePermanentAddressEdtView.text.toString()
            val zipCodePermanentAddress = binding.zipCodePermanentAddressEdtView.text.toString()
            if (permanentFlatNo.isEmpty()) {
                showToast(getString(R.string.lbl_Please_flat_no))
            } else if (permanentAreaAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_area))
            } else if (permanentAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_address))
            } else if (permanentLandmark.isEmpty()) {
                showToast(getString(R.string.lbl_Please_landmark))
            } else if (statePermanentAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_state))
            } else if (cityPermanentAddress.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_city))
            } else if (zipCodePermanentAddress.isEmpty() == true) {
                showToast(getString(R.string.lbl_Please_enter_zipcode))
            } else {
                updateAddressDetailObserver()
            }

        }

    }

    private fun updateAddressDetailObserver() {
        val sameAs = "False"

        val permanentFlatNo = binding.permanentAddressFlatNoEdtView.text.toString()
        val permanentAreaAddress = binding.permanentAddressAreaEdtView.text.toString()
        val permanentAddress = binding.permanentAddressEdtView.text.toString()

        val permanentLandmark = binding.permanentAddressLandmarkEdtView.text.toString()
        val cityPermanentAddress = binding.cityPermanentAddressEdtView.text.toString()
        val statePermanentAddress = binding.statePermanentAddressEdtView.text.toString()
        val zipCodePermanentAddress = binding.zipCodePermanentAddressEdtView.text.toString()
        val jsonObject = JSONObject()

        jsonObject.put(JsonConstants.flatNo, flatNo)
        jsonObject.put(JsonConstants.area, area)
        jsonObject.put(JsonConstants.addAddress, address)
        jsonObject.put(JsonConstants.currentLatitudes, latitude)
        jsonObject.put(JsonConstants.currentLongitudes, longitude)
        jsonObject.put(JsonConstants.landmark, landmark)
        jsonObject.put(JsonConstants.currentState, state)
        jsonObject.put(JsonConstants.city, city)
        jsonObject.put(JsonConstants.zipCode, zipCode)
        jsonObject.put(JsonConstants.sameAddressCheckBox, sameAs)
        jsonObject.put(JsonConstants.permanentFlatNo, permanentFlatNo)
        jsonObject.put(JsonConstants.permanentAreaAddress, permanentAreaAddress)
        jsonObject.put(JsonConstants.permanentAddress, permanentAddress)
        jsonObject.put(JsonConstants.permanentLatitudes, permanentlatitude)
        jsonObject.put(JsonConstants.permanentLongitudes, permanentlongitude)
        jsonObject.put(JsonConstants.permanentLandmark, permanentLandmark)
        jsonObject.put(JsonConstants.cityPermanentAddress, cityPermanentAddress)
        jsonObject.put(JsonConstants.statePermanentAddress, statePermanentAddress)
        jsonObject.put(JsonConstants.permanentZipCode, zipCodePermanentAddress)

        if (Utils.checkConnection(this@AddPermanetAddressActivity)) {
            uploadDocumentViewModel.updateAddressDetailResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@AddPermanetAddressActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!uploadDocumentViewModel.updateAddressDetailData.hasObservers()) {
            uploadDocumentViewModel.updateAddressDetailData.observe(this@AddPermanetAddressActivity)
            { updateAddressDetailData ->
                updateAddressDetailData.let {
                    if (updateAddressDetailData.success) {
                        Toast.makeText(
                            this@AddPermanetAddressActivity,
                            getString(R.string.msg_update_address),
                            Toast.LENGTH_SHORT
                        ).show()
                        val activityIntent = Intent(
                            this@AddPermanetAddressActivity,
                            GetAddressDetailActivity::class.java
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
                                Utils.showProgress(this@AddPermanetAddressActivity)
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

    private fun addAddressDetailObserver() {

        val sameAs = "False"

        val permanentFlatNo = binding.permanentAddressFlatNoEdtView.text.toString()
        val permanentAreaAddress = binding.permanentAddressAreaEdtView.text.toString()
        val permanentAddress = binding.permanentAddressEdtView.text.toString()

        val permanentLandmark = binding.permanentAddressLandmarkEdtView.text.toString()
        val cityPermanentAddress = binding.cityPermanentAddressEdtView.text.toString()
        val statePermanentAddress = binding.statePermanentAddressEdtView.text.toString()
        val zipCodePermanentAddress = binding.zipCodePermanentAddressEdtView.text.toString()
        val jsonObject = JSONObject()

        jsonObject.put(JsonConstants.flatNo, flatNo)
        jsonObject.put(JsonConstants.area, area)
        jsonObject.put(JsonConstants.addAddress, address)
        jsonObject.put(JsonConstants.currentLatitudes, latitude)
        jsonObject.put(JsonConstants.currentLongitudes, longitude)
        jsonObject.put(JsonConstants.landmark, landmark)
        jsonObject.put(JsonConstants.currentState, state)
        jsonObject.put(JsonConstants.city, city)
        jsonObject.put(JsonConstants.zipCode, zipCode)
        jsonObject.put(JsonConstants.sameAddressCheckBox, sameAs)
        jsonObject.put(JsonConstants.permanentFlatNo, permanentFlatNo)
        jsonObject.put(JsonConstants.permanentAreaAddress, permanentAreaAddress)
        jsonObject.put(JsonConstants.permanentAddress, permanentAddress)
        jsonObject.put(JsonConstants.permanentLatitudes, permanentlatitude)
        jsonObject.put(JsonConstants.permanentLongitudes, permanentlongitude)
        jsonObject.put(JsonConstants.permanentLandmark, permanentLandmark)
        jsonObject.put(JsonConstants.cityPermanentAddress, cityPermanentAddress)
        jsonObject.put(JsonConstants.statePermanentAddress, statePermanentAddress)
        jsonObject.put(JsonConstants.permanentZipCode, zipCodePermanentAddress)

        if (Utils.checkConnection(this@AddPermanetAddressActivity)) {
            uploadDocumentViewModel.addAddressDetailResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@AddPermanetAddressActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!uploadDocumentViewModel.addAddressDetailData.hasObservers()) {
            uploadDocumentViewModel.addAddressDetailData.observe(this@AddPermanetAddressActivity)
            { addAddressDetailData ->
                addAddressDetailData.let {
                    if (addAddressDetailData.success) {
                        Toast.makeText(
                            this@AddPermanetAddressActivity,
                            getString(R.string.msg_add_address),
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.saveAddAddressDetailBtn.visibility = View.GONE
                        val activityIntent = Intent(
                            this@AddPermanetAddressActivity,
                            AddBankDetailsActivity::class.java
                        )
                        activityIntent.putExtra("type", 1);
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(activityIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        binding.saveAddAddressDetailBtn.visibility = View.VISIBLE

                    }
                }
            }

            if (!uploadDocumentViewModel.loadingState.hasObservers()) {
                uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                    when (loadingState.status) {
                        LoadingState.Status.RUNNING -> {
                            if (!Utils.isProgressShowing()) {
                                Utils.showProgress(this@AddPermanetAddressActivity)
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