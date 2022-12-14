package com.greypixstudio.broovisdeliveryapp.viewmodel.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.greypixstudio.broovisdeliveryapp.model.notification.markasread.NotificationMarkResponse
import com.greypixstudio.broovisdeliveryapp.model.loading.LoadingState
import com.greypixstudio.broovisdeliveryapp.model.notification.notificationclear.NotificationClearResponse
import com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist.NotificationListResponse
import com.greypixstudio.broovisdeliveryapp.utils.api.APIResponse
import com.greypixstudio.broovisdeliveryapp.utils.api.AppResult
import com.greypixstudio.broovisdeliveryapp.utils.api.ErrorData
import kotlinx.coroutines.launch


class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    private val mLoadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = mLoadingState

    val notificationResponse = MutableLiveData<NotificationListResponse>()
    val notificationClearResponse = MutableLiveData<NotificationClearResponse>()
    val notificationMarkResponse = MutableLiveData<NotificationMarkResponse>()


    fun getNotification() {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getNotification()
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val apiResponse = gson.fromJson<NotificationListResponse>(
                                responseJson,
                                NotificationListResponse::class.java
                            )
                            notificationResponse.value = apiResponse
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

    fun clearNotification() {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.clearNotification()
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val apiResponse = gson.fromJson<NotificationClearResponse>(
                                responseJson,
                                NotificationClearResponse::class.java
                            )
                            notificationClearResponse.value = apiResponse
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

    fun markAsRead(id: String,readStatus: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.markAsRead(id,readStatus)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val apiResponse = gson.fromJson<NotificationMarkResponse>(
                                responseJson,
                                NotificationMarkResponse::class.java
                            )
                            notificationMarkResponse.value = apiResponse
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
