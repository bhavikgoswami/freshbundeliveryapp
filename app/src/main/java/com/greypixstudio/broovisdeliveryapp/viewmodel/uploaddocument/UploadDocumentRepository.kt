package com.greypixstudio.broovisdeliveryapp.viewmodel.uploaddocument

import android.content.Context
import com.greypixstudio.broovisdeliveryapp.network.UploadDocumentAPIInterface
import com.greypixstudio.broovisdeliveryapp.utils.api.APIUtils
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.NetworkUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadDocumentRepository(val api: UploadDocumentAPIInterface, private val context: Context) {

    suspend fun addLicenseDetail(
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
    ): AppResult<Any> {
        return try {
            val apiResponse = api.addLicenseDetail(NetworkUtils.getHeaders(),documentType,licenceType,deliveryUsername,number,licenceRegisterDate,licenceExpiryDate,front_side,frontSideRequestBody,back_side,backSideRequestBody)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }


    suspend fun getLicenseDetail(getLicenseDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.getLicenseDetail(NetworkUtils.getHeaders(), getLicenseDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun updateLicenseDetail(
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
    ): AppResult<Any> {
        return try {
            val apiResponse = api.updateLicenseDetail(NetworkUtils.getHeaders(),documentType,licenceType,deliveryUsername,number,licenceRegisterDate,licenceExpiryDate,front_side,frontSideRequestBody,back_side,backSideRequestBody)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }


    suspend fun addAadhaarCardDetail(
        documentType: RequestBody,
        aadhaarCardUserName: RequestBody,
        aadhaarCardNumber: RequestBody,
        front_side: MultipartBody.Part?,
        frontSideRequestBody: RequestBody?,
        back_side: MultipartBody.Part?,
        backSideRequestBody: RequestBody?
    ): AppResult<Any> {
        return try {
            val apiResponse =
                api.addAadhaarCardDetail(NetworkUtils.getHeaders(),documentType,aadhaarCardUserName,aadhaarCardNumber,front_side,frontSideRequestBody,back_side,backSideRequestBody)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun getAdharCardDetail(getAdharCardDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.getAdharCardDetail(NetworkUtils.getHeaders(), getAdharCardDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun updateAadhaarCardDetail(
        documentType: RequestBody,
        aadhaarCardUserName: RequestBody,
        aadhaarCardNumber: RequestBody,
        front_side: MultipartBody.Part?,
        frontSideRequestBody: RequestBody?,
        back_side: MultipartBody.Part?,
        backSideRequestBody: RequestBody?
    ): AppResult<Any> {
        return try {
            val apiResponse =
                api.updateAadhaarCardDetail(NetworkUtils.getHeaders(),documentType,aadhaarCardUserName,aadhaarCardNumber,front_side,frontSideRequestBody,back_side,backSideRequestBody)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun addPanCardDetail(documentType: RequestBody,
                                 panCardUserName: RequestBody,
                                 panCardNumber: RequestBody,
                                 front_side: MultipartBody.Part?,
                                 requestBody: RequestBody?
                                 ): AppResult<Any> {
        return try {
            val apiResponse =
                api.addPanCardDetail(NetworkUtils.getHeaders(),documentType, panCardUserName, panCardNumber,front_side,requestBody)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun addAddressDetail(addAddressDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.addAddressDetail(NetworkUtils.getHeaders(), addAddressDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun getAddressDetail(addressDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.getAddressDetail(NetworkUtils.getHeaders(), addressDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun updateAddressDetail(addressDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.updateAddressDetail(NetworkUtils.getHeaders(), addressDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }
    suspend fun bankListDetail(bankListDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.bankListDetail(NetworkUtils.getHeaders(), bankListDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun addBankDetail(
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
    ): AppResult<Any> {
        return try {
            val apiResponse =
                api.addBankDetail(
                    NetworkUtils.getHeaders(),
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
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun getBankDetail(bankDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.getBankDetail(NetworkUtils.getHeaders(), bankDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun documentListDetail(documentListDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.documentListDetail(NetworkUtils.getHeaders(), documentListDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }
}