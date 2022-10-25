package com.greypixstudio.broovisdeliveryapp.viewmodel.notification

import android.content.Context
import com.greypixstudio.broovisdeliveryapp.network.NotificationInterface
import com.greypixstudio.broovisdeliveryapp.utils.api.APIUtils
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.NetworkUtils

class NotificationRepository (val api: NotificationInterface, private val context: Context) {

    suspend fun getNotification(): AppResult<Any> {
        return try {
            val apiResponse = api.getNotification(NetworkUtils.getHeaders())
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun clearNotification(): AppResult<Any> {
        return try {
            val apiResponse = api.clearNotification(NetworkUtils.getHeaders())
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }


    suspend fun markAsRead(id:String,readStatus: String): AppResult<Any> {
        return try {
            val apiResponse = api.markAsRead(NetworkUtils.getHeaders(),id,readStatus)
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