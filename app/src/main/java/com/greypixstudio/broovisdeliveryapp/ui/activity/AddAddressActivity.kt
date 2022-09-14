package com.greypixstudio.broovisdeliveryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.AddAddressActivityBinding
import com.greypixstudio.broovisdeliveryapp.databinding.ToolbarLayoutBinding
import com.greypixstudio.broovisdeliveryapp.model.JsonConstants
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.AddressData
import com.greypixstudio.broovisdeliveryapp.ui.base.BaseActivity
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import com.greypixstudio.broovisdeliveryapp.utils.Utils
import com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument.UploadDocumentViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_add_address.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAddressActivity : BaseActivity() {

    private lateinit var binding: AddAddressActivityBinding
    private val uploadDocumentViewModel by viewModel<UploadDocumentViewModel>()
    private lateinit var toolbarLayoutBinding: ToolbarLayoutBinding


    private var type: Int = 1
    private var isSameAs = false
    private var sameAsCurrent = "True"

    private var latitude = 0.0
    private var longitude = 0.0


    private var permanentFlatNo = ""
    private var permanentAreaAddress = ""
    private var permanentAddress = ""
    private var permanentLandmark = ""
    private var cityPermanentAddress = ""
    private var statePermanentAddress = ""
    private var zipCodePermanentAddress = ""
    private var permanentlatitude = ""
    private var permanentlongitude = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address)
        init()


        if (Paper.book().contains(Constants.CURRENT_ADDRESS_DATA)) {
            binding.sameAsCurrentAddressCheckBox.isChecked = true
            val currentAddressData = Paper.book().read<AddressData>(Constants.CURRENT_ADDRESS_DATA)
            val flatNo = currentAddressData.currentBuildingName
            val area = currentAddressData.currentArea
            val address = currentAddressData.currentAddress
            val landmark = currentAddressData.currentLandmark
            latitude = currentAddressData.currentLatitudes
            longitude = currentAddressData.currentLongitudes
            val state = currentAddressData.currentState
            val city = currentAddressData.currentCity
            val zipCode = currentAddressData.currentZipcode

            binding.addressEdtView.setText(address)
            binding.landmarkEdtView.setText(area)
            binding.flatNoEdtView.setText(flatNo)
            binding.areaEdtView.setText(landmark)
            binding.stateEdtView.setText(state)
            binding.cityEdtView.setText(city)
            binding.zipCodeEdtView.setText(zipCode)
        }



        binding.addLocationPermanentAddressBtn.setOnClickListener {
            val flatNo = binding.flatNoEdtView.text.toString()
            val area = binding.areaEdtView.text.toString()
            val address = binding.addressEdtView.text.toString()
            val landmark = binding.landmarkEdtView.text.toString()
            val state = binding.stateEdtView.text.toString()
            val city = binding.cityEdtView.text.toString()
            val zipCode = binding.zipCodeEdtView.text.toString()
            if (flatNo.isEmpty()) {
                showToast(getString(R.string.lbl_Please_flat_no))
            } else if (area.isEmpty()) {
                showToast(getString(R.string.lbl_Please_area))
            } else if (address.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_address))
            } else if (landmark.isEmpty()) {
                showToast(getString(R.string.lbl_Please_landmark))
            } else if (state.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_state))
            } else if (city.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_city))
            } else if (zipCode.isEmpty() == true) {
                showToast(getString(R.string.lbl_Please_enter_zipcode))
            } else {
                val activityIntent =
                    Intent(this@AddAddressActivity, UseCurrentAddressActivity::class.java)
                activityIntent.putExtra("type", 2)
                startActivity(activityIntent)
                overridePendingTransition(0, 0)
                finish()
            }
        }

        binding.saveAddAddressDetailBtn.setOnClickListener {

            val flatNo = binding.flatNoEdtView.text.toString()
            val area = binding.areaEdtView.text.toString()
            val address = binding.addressEdtView.text.toString()
            val landmark = binding.landmarkEdtView.text.toString()
            val state = binding.stateEdtView.text.toString()
            val city = binding.cityEdtView.text.toString()
            val zipCode = binding.zipCodeEdtView.text.toString()


            if (flatNo.isEmpty()) {
                showToast(getString(R.string.lbl_Please_flat_no))
            } else if (area.isEmpty()) {
                showToast(getString(R.string.lbl_Please_area))
            } else if (address.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_address))
            } else if (landmark.isEmpty()) {
                showToast(getString(R.string.lbl_Please_landmark))
            } else if (state.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_state))
            } else if (city.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_city))
            } else if (zipCode.isEmpty() == true) {
                showToast(getString(R.string.lbl_Please_enter_zipcode))
            } else {
                isSameAs = binding.sameAsCurrentAddressCheckBox.isChecked
                if (isSameAs) {
                    addAddressDetailObserver()
                }
            }
        }

        binding.saveAddAddressUpdateBtn.setOnClickListener {
            val flatNo = binding.flatNoEdtView.text.toString()
            val area = binding.areaEdtView.text.toString()
            val address = binding.addressEdtView.text.toString()
            val landmark = binding.landmarkEdtView.text.toString()
            val state = binding.stateEdtView.text.toString()
            val city = binding.cityEdtView.text.toString()
            val zipCode = binding.zipCodeEdtView.text.toString()


            if (flatNo.isEmpty()) {
                showToast(getString(R.string.lbl_Please_flat_no))
            } else if (area.isEmpty()) {
                showToast(getString(R.string.lbl_Please_area))
            } else if (address.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_address))
            } else if (landmark.isEmpty()) {
                showToast(getString(R.string.lbl_Please_landmark))
            } else if (state.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_state))
            } else if (city.isEmpty()) {
                showToast(getString(R.string.lbl_Please_enter_city))
            } else if (zipCode.isEmpty() == true) {
                showToast(getString(R.string.lbl_Please_enter_zipcode))
            } else {
                updateAddressDetailObserver()
            }


        }
    }


    private fun init() {

        binding.saveAddAddressUpdateBtn.visibility = View.GONE
        binding.saveAddAddressDetailBtn.visibility = View.GONE
        binding.addLocationPermanentAddressBtn.visibility = View.VISIBLE

        toolbarLayoutBinding = binding.addAddressToolbar
        binding.addAddressToolbar.toolbarNametxtView.text = getText(R.string.lbl_add_address)
        binding.addAddressToolbar.backACImageView.setOnClickListener {
            onBackPressed()
        }




        if (intent.hasExtra("type")) {
            type = intent.getIntExtra("type", 1)
            if (type == 1) {
                binding.sameAsCurrentAddressCheckBox.setOnClickListener {
                    isSameAs = sameAsCurrentAddressCheckBox.isChecked
                    if (!isSameAs) {
                        binding.saveAddAddressDetailBtn.visibility = View.GONE
                        binding.addLocationPermanentAddressBtn.visibility = View.VISIBLE
                    } else {
                        binding.addLocationPermanentAddressBtn.visibility = View.GONE
                        binding.saveAddAddressUpdateBtn.visibility = View.GONE
                        binding.saveAddAddressDetailBtn.visibility = View.VISIBLE
                    }
                }
                binding.saveAddAddressUpdateBtn.visibility = View.GONE
                binding.saveAddAddressDetailBtn.visibility = View.VISIBLE
                binding.addLocationPermanentAddressBtn.visibility = View.GONE
            }

            if (type == 3) {
                getAddressDetail()
                binding.saveAddAddressUpdateBtn.visibility = View.VISIBLE
                binding.permanentAddressTxtView.visibility = View.GONE
                binding.addLocationPermanentAddressBtn.visibility = View.GONE
                binding.saveAddAddressDetailBtn.visibility = View.GONE
                binding.sameAsCurrentAddressCheckBox.visibility = View.GONE

            }

        }

    }


    private fun getAddressDetail() {
        if (Utils.checkConnection(this@AddAddressActivity)) {
            uploadDocumentViewModel.getAddressDetailResponse(toString())
        } else {
            Toast.makeText(
                this@AddAddressActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }


        if (!uploadDocumentViewModel.getAddressDetailData.hasObservers()) {
            uploadDocumentViewModel.getAddressDetailData.observe(this@AddAddressActivity)
            { getAddressDetailData ->
                getAddressDetailData.let {
                    if (getAddressDetailData.success) {

                        permanentFlatNo = getAddressDetailData.results.records.permanentBuildingName
                        permanentAreaAddress = getAddressDetailData.results.records.permanentArea
                        permanentAddress = getAddressDetailData.results.records.permanentAddress
                        permanentLandmark = getAddressDetailData.results.records.permanentLandmark
                        permanentlatitude = getAddressDetailData.results.records.permanentLatitudes
                        permanentlongitude = getAddressDetailData.results.records.permanentLongitudes
                        statePermanentAddress = getAddressDetailData.results.records.permanentState
                        cityPermanentAddress = getAddressDetailData.results.records.permanentCity
                        zipCodePermanentAddress = getAddressDetailData.results.records.permanentZipcode.toString()


                    }
                }

                if (!uploadDocumentViewModel.loadingState.hasObservers()) {
                    uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                        when (loadingState.status) {
                            LoadingState.Status.RUNNING -> {
                                if (!Utils.isProgressShowing()) {
                                    Utils.showProgress(this@AddAddressActivity)
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

    private fun updateAddressDetailObserver() {
        val flatNo = binding.flatNoEdtView.text.toString()
        val area = binding.areaEdtView.text.toString()
        val address = binding.addressEdtView.text.toString()
        val landmark = binding.landmarkEdtView.text.toString()
        val state = binding.stateEdtView.text.toString()
        val city = binding.cityEdtView.text.toString()
        val zipCode = binding.zipCodeEdtView.text.toString()
        val sameAs = "False"

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

        if (Utils.checkConnection(this@AddAddressActivity)) {
            uploadDocumentViewModel.updateAddressDetailResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@AddAddressActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!uploadDocumentViewModel.updateAddressDetailData.hasObservers()) {
            uploadDocumentViewModel.updateAddressDetailData.observe(this@AddAddressActivity)
            { updateAddressDetailData ->
                updateAddressDetailData.let {
                    if (updateAddressDetailData.success) {
                        Toast.makeText(
                            this@AddAddressActivity,
                            getString(R.string.msg_update_address),
                            Toast.LENGTH_SHORT
                        ).show()
                        val activityIntent = Intent(
                            this@AddAddressActivity,
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
                                Utils.showProgress(this@AddAddressActivity)
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

        val flatNo = binding.flatNoEdtView.text.toString()
        val area = binding.areaEdtView.text.toString()
        val address = binding.addressEdtView.text.toString()

        val landmark = binding.landmarkEdtView.text.toString()
        val state = binding.stateEdtView.text.toString()
        val city = binding.cityEdtView.text.toString()
        val zipCode = binding.zipCodeEdtView.text.toString()
        val sameAs = binding.sameAsCurrentAddressCheckBox.isChecked
        isSameAs = sameAs
        if (isSameAs)
            sameAsCurrent = "True"
        else
            sameAsCurrent = "False"

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
        jsonObject.put(JsonConstants.sameAddressCheckBox, sameAsCurrent)

        if (Utils.checkConnection(this@AddAddressActivity)) {
            uploadDocumentViewModel.addAddressDetailResponse(jsonObject.toString())
        } else {
            Toast.makeText(
                this@AddAddressActivity,
                getString(R.string.msg_internet_connection_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }


        if (!uploadDocumentViewModel.addAddressDetailData.hasObservers()) {
            uploadDocumentViewModel.addAddressDetailData.observe(this@AddAddressActivity)
            { addAddressDetailData ->
                addAddressDetailData.let {
                    if (addAddressDetailData.success) {
                        Toast.makeText(
                            this@AddAddressActivity,
                            getString(R.string.msg_add_address),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.saveAddAddressUpdateBtn.visibility = View.VISIBLE
                        binding.saveAddAddressDetailBtn.visibility = View.GONE
                        binding.addLocationPermanentAddressBtn.visibility = View.GONE

                        val activityIntent = Intent(
                            this@AddAddressActivity,
                            AddBankDetailsActivity::class.java
                        )
                        activityIntent.putExtra("type", 1);
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(activityIntent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        binding.saveAddAddressUpdateBtn.visibility = View.GONE
                        binding.saveAddAddressDetailBtn.visibility = View.VISIBLE
                        binding.addLocationPermanentAddressBtn.visibility = View.GONE
                    }
                }
            }

            if (!uploadDocumentViewModel.loadingState.hasObservers()) {
                uploadDocumentViewModel.loadingState.observe(this) { loadingState ->

                    when (loadingState.status) {
                        LoadingState.Status.RUNNING -> {
                            if (!Utils.isProgressShowing()) {
                                Utils.showProgress(this@AddAddressActivity)
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