package com.greypixstudio.broovisdeliveryapp.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.greypixstudio.broovisdeliveryapp.model.auth.getuserdetail.GetUserDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.logout.LogoutResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.updatefirebase.UpdateFirebaseTokenData
import com.greypixstudio.broovisdeliveryapp.model.auth.sendotp.SendOTPResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.setmpin.SetMpinResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.signup.SignUpResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.updateuserdetail.UpdateUserDetailResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.verifympin.VerifyMpinResponse
import com.greypixstudio.broovisdeliveryapp.model.auth.verifyotp.VerifyOTPResponse
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.utils.api.APIResponse
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.ErrorData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val mLoadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = mLoadingState

    val generateOTPData = MutableLiveData<SendOTPResponse>()
    val verifyOTPData = MutableLiveData<VerifyOTPResponse>()
//    val logoutUserData = MutableLiveData<LogoutUserData>()
    val signUpUserData = MutableLiveData<SignUpResponse>()
    val userDetailUpdateData = MutableLiveData<UpdateUserDetailResponse>()
      val tokenUpdateData = MutableLiveData<UpdateFirebaseTokenData>()
    val getUserDetailData = MutableLiveData<GetUserDetailResponse>()
//    val updateUserData = MutableLiveData<UpdateUserResponse>()
    val setMpinData = MutableLiveData<SetMpinResponse>()
    val verifyMpinData =  MutableLiveData<VerifyMpinResponse>()
    val logoutData = MutableLiveData<LogoutResponse>()

    fun generateOTP(phoneNumber: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.generateOTP(phoneNumber)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val generatedOTPDataResponse = gson.fromJson<SendOTPResponse>(
                                responseJson,
                                SendOTPResponse::class.java
                            )
                            generateOTPData.value = generatedOTPDataResponse
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

    fun verifyOTP(mobile: String, otpValue: String, type: Int) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.verifyOTP(mobile, otpValue, type)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        //   LogCatUtil.appendLog("verifyOTP Response: $responseData")
                        if (responseData.success) {
                            val verifyOTPDataResponse = gson.fromJson<VerifyOTPResponse>(
                                responseJson,
                                VerifyOTPResponse::class.java
                            )
                            verifyOTPData.value = verifyOTPDataResponse
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

    fun logoutUser() {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.logoutUser()
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
//                            val logoutUserDataResponse = gson.fromJson<LogoutUserData>(
//                                responseJson,
//                                LogoutUserData::class.java
//                            )
//                            logoutUserData.value = logoutUserDataResponse
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

//    fun signupUser( type: RequestBody,
//                    first_name: RequestBody,
//                    last_name: RequestBody,
//                    primary_contact_no: RequestBody,
//                    email: RequestBody,
//                    birthdate: RequestBody,
//                    has_license: RequestBody,
//                    image: MultipartBody.Part?,
//                    imgRequestBody: RequestBody?) {
//        mLoadingState.postValue(LoadingState.LOADING)
//        viewModelScope.launch {
//            val result = repository.signupUser(type, first_name, last_name, primary_contact_no, email, birthdate, has_license, image, imgRequestBody)
//            when (result) {
//                is AppResult.Success -> {
//                    mLoadingState.postValue(LoadingState.LOADED)
//                    result.successData.let {
//                        val gson = GsonBuilder().create()
//                        val responseJson = gson.toJson(it)
//                        val responseData =
//                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
//                        if (responseData.success) {
//                            val signupUserDataResponse = gson.fromJson<SignUpResponse>(
//                                responseJson,
//                                SignUpResponse::class.java
//                            )
//                            signUpUserData.value = signupUserDataResponse
//                        } else {
//                            val errorData =
//                                gson.fromJson<ErrorData>(responseJson, ErrorData::class.java)
//                            mLoadingState.postValue(LoadingState.error(errorData))
//                        }
//                    }
//                }
//                is AppResult.Error -> {
//                    mLoadingState.postValue(LoadingState.error(result.errorData))
//                }
//            }
//        }
//    }

    fun updateFirebaseToken(firebaseToken: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.updateFirebaseToken(firebaseToken)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val signupUserDataResponse = gson.fromJson<UpdateFirebaseTokenData>(
                               responseJson,
                               UpdateFirebaseTokenData::class.java
                           )
                           tokenUpdateData.value = signupUserDataResponse
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

    fun getUserDetails() {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getUserDetails()
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                           val getUserDetailDataResponse = gson.fromJson<GetUserDetailResponse>(
                               responseJson,
                               GetUserDetailResponse::class.java
                          )
                            getUserDetailData.value = getUserDetailDataResponse
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

    fun signupUser(
        type: RequestBody,
        first_name: RequestBody,
        last_name: RequestBody,
        primary_contact_no: RequestBody,
        email: RequestBody,
        birthdate: RequestBody,
        gender: RequestBody,
        device_type: RequestBody,
        has_license: RequestBody,
        image: MultipartBody.Part?,
        imgRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.signupUser(type, first_name, last_name, primary_contact_no, email, birthdate,gender,device_type, has_license, image, imgRequestBody)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val signupUserDataResponse = gson.fromJson<SignUpResponse>(
                                responseJson,
                                SignUpResponse::class.java
                            )
                            signUpUserData.value = signupUserDataResponse
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

    fun userDetailUpdate(
        first_name: RequestBody,
        last_name: RequestBody,
        primary_contact_no: RequestBody,
        alternative_contact_no:RequestBody,
        email: RequestBody,
        birthdate: RequestBody,
        no_of_family_member: RequestBody,
        device_type: RequestBody,
        gender: RequestBody,
        image: MultipartBody.Part?,
        imgRequestBody: RequestBody?
    ) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.userDetailUpdate(first_name, last_name, primary_contact_no,alternative_contact_no, email, birthdate,no_of_family_member,device_type,gender ,image, imgRequestBody)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val userDetailUpdateDataResponse = gson.fromJson<UpdateUserDetailResponse>(
                                responseJson,
                                UpdateUserDetailResponse::class.java
                            )
                            userDetailUpdateData.value = userDetailUpdateDataResponse
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
    fun setMpinResponse(setMpinRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.setMpin(setMpinRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val setMpinDataResponse = gson.fromJson<SetMpinResponse>(
                                responseJson,
                                SetMpinResponse::class.java
                            )
                            setMpinData.value = setMpinDataResponse
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

    fun verifyMpinResponse(verifyMpinRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.verifyMpin(verifyMpinRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val verifyMpinDataResponse = gson.fromJson<VerifyMpinResponse>(
                                responseJson,
                                VerifyMpinResponse::class.java
                            )
                            verifyMpinData.value = verifyMpinDataResponse
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


    fun logoutResponse() {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.logout()
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val logoutDataResponse = gson.fromJson<LogoutResponse>(
                                responseJson,
                                LogoutResponse::class.java
                            )
                            logoutData.value = logoutDataResponse
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