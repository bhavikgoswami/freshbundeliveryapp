package com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.AddressData
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addaddressdetail.AddAddressDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addadharcarddetail.AddAdharCardDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addbankdetail.addbank.AddBankDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addbankdetail.banklist.BankListResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addbankdetail.banklist.Record
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addlicensedetail.AddLicenseDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addpancarddetail.AddPanCardDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.documentlistdetail.DocumentListResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getaddressdetail.GetAddressDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getadharcarddetail.GetAdharCardDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getbankdetail.Bank
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getbankdetail.GetBankDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getlincencedetail.GetLincenceDetaillResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.updateaddressdetail.UpdateAddressDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.updateadharcard.UpdateAdharCardDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.uploaddocument.updatelincencedetail.UpdatlLincenceDetailResponse
import com.greypixstudio.broovisdeliveryapp.utils.api.APIResponse
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.ErrorData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadDocumentViewModel(private val repository: UploadDocumentRepository) : ViewModel() {

    private val mLoadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = mLoadingState

    val addLicenseDetailData = MutableLiveData<AddLicenseDetailResponse>()
    val getLicenseDetailData = MutableLiveData<GetLincenceDetaillResponse>()
    val updateLicenseDetailData = MutableLiveData<UpdatlLincenceDetailResponse>()
    val addAadhaarCardDetailData = MutableLiveData<AddAdharCardDetailResponse>()
    val getAadhaarCardDetailData = MutableLiveData<GetAdharCardDetailResponse>()
    val updateAadhaarCardDetailData = MutableLiveData<UpdateAdharCardDetailResponse>()
    val addPanCardDetailData = MutableLiveData<AddPanCardDetailResponse>()
    val documentListDetailData = MutableLiveData<DocumentListResponse>()
    val addAddressDetailData = MutableLiveData<AddAddressDetailResponse>()
    val getAddressDetailData = MutableLiveData<GetAddressDetailResponse>()
    val updateAddressDetailData = MutableLiveData<UpdateAddressDetailResponse>()
    val bankListDetailData = MutableLiveData<BankListResponse>()
    val addBankDetailData = MutableLiveData<AddBankDetailResponse>()
    val getBankDetailData = MutableLiveData<GetBankDetailResponse>()
    var bank: Record? = null
    fun setBankDetails(record: Record?) {
        this.bank = record
    }

    fun getBankDetails(): Record? {
        return bank
    }

    fun addAddressDetailResponse(addAddressDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.addAddressDetail(addAddressDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val addAddressDetailDataResponse =
                                gson.fromJson<AddAddressDetailResponse>(
                                    responseJson,
                                    AddAddressDetailResponse::class.java
                                )
                            addAddressDetailData.value = addAddressDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun getAddressDetailResponse(addressDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getAddressDetail(addressDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val getAddressDetailDataResponse =
                                gson.fromJson<GetAddressDetailResponse>(
                                    responseJson,
                                    GetAddressDetailResponse::class.java
                                )
                            getAddressDetailData.value = getAddressDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun updateAddressDetailResponse(addressDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.updateAddressDetail(addressDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val updateAddressDetailDataResponse =
                                gson.fromJson<UpdateAddressDetailResponse>(
                                    responseJson,
                                    UpdateAddressDetailResponse::class.java
                                )
                            updateAddressDetailData.value = updateAddressDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun bankListDetailResponse(bankListDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.bankListDetail(bankListDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val bankListDetailDataResponse = gson.fromJson<BankListResponse>(
                                responseJson,
                                BankListResponse::class.java
                            )
                            bankListDetailData.value = bankListDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }


    fun addBnkDetailResponse(
        bankId: RequestBody?,
        bankAccountUserName: RequestBody?,
        accountNumber: RequestBody?,
        accountType: RequestBody?,
        branchName: RequestBody?,
        ifscCode: RequestBody?,
        micrCode: RequestBody?,
        branchCode: RequestBody?,
        bankFlag: RequestBody?,
        fileMultipart: MultipartBody.Part?,
        imgRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.addBankDetail(
                bankId,
                bankAccountUserName,
                accountNumber,
                accountType,
                branchName,
                ifscCode,
                micrCode,
                branchCode,
                bankFlag,
                fileMultipart,
                imgRequestBody
            )
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val addBankDetailDataResponse = gson.fromJson<AddBankDetailResponse>(
                                responseJson,
                                AddBankDetailResponse::class.java
                            )
                            addBankDetailData.value = addBankDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun getBankDetailResponse(bankDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getBankDetail(bankDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val getBankDetailDataResponse = gson.fromJson<GetBankDetailResponse>(
                                responseJson,
                                GetBankDetailResponse::class.java
                            )
                            getBankDetailData.value = getBankDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun addLicenseDetailResponse(
        documentType: RequestBody,
        licenceType: RequestBody,
        deliveryUsername: RequestBody,
        number: RequestBody,
        licenceRegisterDate: RequestBody,
        licenceExpiryDate: RequestBody,
        front_side: MultipartBody.Part?,
        frontSideRequestBody: RequestBody?,
        back_side: MultipartBody.Part?,
        backSideRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.addLicenseDetail(
                documentType,
                licenceType,
                deliveryUsername,
                number,
                licenceRegisterDate,
                licenceExpiryDate,
                front_side,
                frontSideRequestBody,
                back_side,
                backSideRequestBody
            )
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val addLicenseDetailDataResponse =
                                gson.fromJson<AddLicenseDetailResponse>(
                                    responseJson,
                                    AddLicenseDetailResponse::class.java
                                )
                            addLicenseDetailData.value = addLicenseDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun getLicenceDetailResponse(getLicenceDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getLicenseDetail(getLicenceDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val getLicenseDetailDataResponse =
                                gson.fromJson<GetLincenceDetaillResponse>(
                                    responseJson,
                                    GetLincenceDetaillResponse::class.java
                                )
                            getLicenseDetailData.value = getLicenseDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun updateLicenseDetailResponse(
        documentType: RequestBody,
        licenceType: RequestBody,
        deliveryUsername: RequestBody,
        number: RequestBody,
        licenceRegisterDate: RequestBody,
        licenceExpiryDate: RequestBody,
        front_side: MultipartBody.Part?,
        frontSideRequestBody: RequestBody?,
        back_side: MultipartBody.Part?,
        backSideRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.updateLicenseDetail(
                documentType,
                licenceType,
                deliveryUsername,
                number,
                licenceRegisterDate,
                licenceExpiryDate,
                front_side,
                frontSideRequestBody,
                back_side,
                backSideRequestBody
            )
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val updateLicenseDetailDataResponse =
                                gson.fromJson<UpdatlLincenceDetailResponse>(
                                    responseJson,
                                    UpdatlLincenceDetailResponse::class.java
                                )
                            updateLicenseDetailData.value = updateLicenseDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }


    fun addAadhaarCardDetailResponse(
        documentType: RequestBody,
        aadhaarCardUserName: RequestBody,
        aadhaarCardNumber: RequestBody,
        front_side: MultipartBody.Part?,
        frontSideRequestBody: RequestBody?,
        back_side: MultipartBody.Part?,
        backSideRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.addAadhaarCardDetail(
                documentType,
                aadhaarCardUserName,
                aadhaarCardNumber,
                front_side,
                frontSideRequestBody,
                back_side,
                backSideRequestBody
            )
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val addAadhaarCardDetailDataResponse =
                                gson.fromJson<AddAdharCardDetailResponse>(
                                    responseJson,
                                    AddAdharCardDetailResponse::class.java
                                )
                            addAadhaarCardDetailData.value = addAadhaarCardDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun getAdharCardDetailResponse(getAdharCardDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getAdharCardDetail(getAdharCardDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val getAadhaarCardDetailDataResponse =
                                gson.fromJson<GetAdharCardDetailResponse>(
                                    responseJson,
                                    GetAdharCardDetailResponse::class.java
                                )
                            getAadhaarCardDetailData.value = getAadhaarCardDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun updateAadhaarCardDetailResponse(
        documentType: RequestBody,
        aadhaarCardUserName: RequestBody,
        aadhaarCardNumber: RequestBody,
        front_side: MultipartBody.Part?,
        frontSideRequestBody: RequestBody?,
        back_side: MultipartBody.Part?,
        backSideRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.updateAadhaarCardDetail(
                documentType,
                aadhaarCardUserName,
                aadhaarCardNumber,
                front_side,
                frontSideRequestBody,
                back_side,
                backSideRequestBody
            )
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val updateAadhaarCardDetailDataResponse =
                                gson.fromJson<UpdateAdharCardDetailResponse>(
                                    responseJson,
                                    UpdateAdharCardDetailResponse::class.java
                                )
                            updateAadhaarCardDetailData.value = updateAadhaarCardDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }

    fun addPanCardDetailResponse(
        documentType: RequestBody,
        panCardUserName: RequestBody,
        panCardNumber: RequestBody,
        front_side: MultipartBody.Part?,
        requestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.addPanCardDetail(
                documentType,
                panCardUserName,
                panCardNumber,
                front_side,
                requestBody
            )
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val addPanCardDetailDataDataResponse =
                                gson.fromJson<AddPanCardDetailResponse>(
                                    responseJson,
                                    AddPanCardDetailResponse::class.java
                                )
                            addPanCardDetailData.value = addPanCardDetailDataDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }


    fun documentListDetailResponse(documentListDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.documentListDetail(documentListDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val documentListDetailDataResponse =
                                gson.fromJson<DocumentListResponse>(
                                    responseJson,
                                    DocumentListResponse::class.java
                                )
                            documentListDetailData.value = documentListDetailDataResponse
                        } else {
                            val errorData =
                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
                            mLoadingState.postValue(LoadingState.error(errorData))
                        }
                    }
                }
                is AppResult.Error -> {
                    mLoadingState.postValue(LoadingState.error(result.errorData))
                }
            }
        }
    }


}