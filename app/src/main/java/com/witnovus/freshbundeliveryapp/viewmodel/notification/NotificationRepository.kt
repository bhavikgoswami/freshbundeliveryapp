package com.witnovus.freshbundeliveryapp.viewmodel.notification

import android.content.Context
import com.witnovus.freshbundeliveryapp.network.NotificationInterface
import com.witnovus.freshbundeliveryapp.utils.api.APIUtils
import com.witnovus.freshbundeliveryapp.utils.api.AppResult
import com.witnovus.freshbundeliveryapp.utils.api.NetworkUtils

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
}