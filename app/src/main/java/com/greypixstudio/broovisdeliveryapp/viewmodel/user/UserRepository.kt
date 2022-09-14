package com.greypixstudio.broovisdeliveryapp.viewmodel.user

import android.content.Context
import com.greypixstudio.broovisdeliveryapp.network.UserAPIInterface
import com.greypixstudio.broovisdeliveryapp.utils.api.APIUtils
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.NetworkUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(val api: UserAPIInterface, private val context: Context) {

    suspend fun generateOTP(requestBody: String): AppResult<Any> {
        return try {
            val apiResponse = api.generateOTP(requestBody)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun verifyOTP(mobile: String, otpValue: String, type: Int): AppResult<Any> {
        return try {
            val response = api.verifyOTP(mobile, otpValue, type)
            if (response.isSuccessful) {
                APIUtils.handleSuccess(response)
            } else {
                APIUtils.handleApiError(response)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun logoutUser(): AppResult<Any> {
        return try {
            val response = api.logoutUser(NetworkUtils.getHeaders()/*,""*/)
            if (response.isSuccessful) {
                APIUtils.handleSuccess(response)
            } else {
                APIUtils.handleApiError(response)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun signupUser(
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
    ): AppResult<Any> {
        return try {
            val response =
                api.signupUser(type, first_name, last_name, primary_contact_no, email, birthdate,gender, device_type, has_license, image, imgRequestBody)
            if (response.isSuccessful) {
                APIUtils.handleSuccess(response)
            } else {
                APIUtils.handleApiError(response)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun userDetailUpdate(
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
    ): AppResult<Any> {
        return try {
            val response =
                api.userDetailUpdate(NetworkUtils.getHeaders(),first_name, last_name, primary_contact_no,alternative_contact_no, email,birthdate,no_of_family_member,device_type,gender ,image, imgRequestBody)
            if (response.isSuccessful) {
                APIUtils.handleSuccess(response)
            } else {
                APIUtils.handleApiError(response)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun updateFirebaseToken(firebaseToken: String): AppResult<Any> {
        return try {
            val response = api.updateFirebaseToken(NetworkUtils.getHeaders(), firebaseToken)
            println("updateFirebaseToken: $response")
            if (response.isSuccessful) {
                APIUtils.handleSuccess(response)
            } else {
                APIUtils.handleApiError(response)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun getUserDetails(): AppResult<Any> {
        return try {
            val response = api.getUserDetails(NetworkUtils.getHeaders())
            if (response.isSuccessful) {
                APIUtils.handleSuccess(response)
            } else {
                APIUtils.handleApiError(response)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

//    suspend fun updateUserProfile(
//        first_name: RequestBody,
//        last_name: RequestBody,
//        primary_contact_no: RequestBody,
//        email: RequestBody,
//        alternative_contact_no: RequestBody,
//        birthdate: RequestBody,
//        gender: RequestBody,
//        no_of_family_member: RequestBody,
//        device_type_id: RequestBody,
//        image: MultipartBody.Part?,
//        imgRequestBody: RequestBody?
//    ): AppResult<Any> {
//        return try {
//            val response =
//                api.updateUserProfile(NetworkUtils.getHeaders(), first_name, last_name, primary_contact_no, email, alternative_contact_no, birthdate, gender, no_of_family_member, device_type_id, image, imgRequestBody)
//            if (response.isSuccessful) {
//                APIUtils.handleSuccess(response)
//            } else {
//                APIUtils.handleApiError(response)
//            }
//        } catch (e: Exception) {
//            APIUtils.customException(context, e)
//        }
// }
    suspend fun setMpin(setMpinRequestHashmap: String): AppResult<Any> {
    return try {
        val apiResponse = api.setMpin(NetworkUtils.getHeaders(),setMpinRequestHashmap)

          if (apiResponse.isSuccessful) {
            APIUtils.handleSuccess(apiResponse)
        } else {
            APIUtils.handleApiError(apiResponse)
        }
    } catch (e: Exception) {
        APIUtils.customException(context, e)
    }
}
    suspend fun verifyMpin(verifyMpinRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse = api.verifyMpin(NetworkUtils.getHeaders(),verifyMpinRequestHashmap)

            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun logout(): AppResult<Any> {
        return try {
            val apiResponse = api.logout(NetworkUtils.getHeaders())

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