package com.greypixstudio.broovisdeliveryapp.viewmodel.orderdetail

import android.content.Context
import com.greypixstudio.broovisdeliveryapp.network.OrderDetailApiInterface
import com.greypixstudio.broovisdeliveryapp.utils.api.APIUtils
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.NetworkUtils
import retrofit2.http.Field


class OrderDetailRepository(val api: OrderDetailApiInterface, private val context: Context)  {

    suspend fun getOrderListDetail(getOrderListDetailRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.getOrderListDetail(NetworkUtils.getHeaders(), getOrderListDetailRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun orderDelivered(orderDeliveredRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.orderDelivered(NetworkUtils.getHeaders(), orderDeliveredRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun deliveredOrderList(deliveredOrderListRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.deliveredOrderList(NetworkUtils.getHeaders(), deliveredOrderListRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun notDeliveredOrder(notDeliveredOrderRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.notDeliveredOrder(NetworkUtils.getHeaders(), notDeliveredOrderRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun vacationApply(vacationApplyRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.vacationApply(NetworkUtils.getHeaders(), vacationApplyRequestHashmap)
            if (apiResponse.isSuccessful) {
                APIUtils.handleSuccess(apiResponse)
            } else {
                APIUtils.handleApiError(apiResponse)
            }
        } catch (e: Exception) {
            APIUtils.customException(context, e)
        }
    }

    suspend fun vacationVerify(vacationVerifyRequestHashmap: String): AppResult<Any> {
        return try {
            val apiResponse =
                api.vacationVerify(NetworkUtils.getHeaders(),vacationVerifyRequestHashmap)
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