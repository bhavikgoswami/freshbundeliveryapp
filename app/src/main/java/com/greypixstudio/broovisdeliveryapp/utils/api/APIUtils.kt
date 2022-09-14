package com.greypixstudio.broovisdeliveryapp.utils.api

import android.content.Context
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.utils.Constants
import retrofit2.HttpException
import retrofit2.Response

object APIUtils {

    private const val ERROR_BAD_REQUEST = 400
    private const val ERROR_UNAUTHORIZED = 401
    private const val ERROR_NOT_FOUND = 404
    private const val ERROR_METHOD_NOT_ALLOWED = 405

    fun <T : Any> handleApiError(response: Response<T>): AppResult<T> {
        val error: ErrorData = ApiErrorUtils.parseError(response)
        return AppResult.Error(error)
    }

    fun <T : Any> handleSuccess(response: Response<T>): AppResult<T> {
        response.body()?.let {
            return AppResult.Success(it)
        } ?: return handleApiError(response)
    }

    fun customException(context: Context, exception: Exception): AppResult<Any> {
        return try {
            var errorMessage = exception.message
            var errorCode = (exception as HttpException).code()
            val error = ErrorData(null, errorCode, errorMessage!!, false)
            AppResult.Error(error)
        } catch (exception: Exception) {


            var errorMessage = "Something went wrong, Please try again."
            var errorCode = Constants.CUSTOM_EXCEPTION_ERROR_CODE
            val error = ErrorData(null, errorCode, errorMessage, false)
            AppResult.Error(error)
        }
    }
}