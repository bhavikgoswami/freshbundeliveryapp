package com.witnovus.freshbundeliveryapp.viewmodel.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.witnovus.freshbundeliveryapp.model.loading.LoadingState
import com.witnovus.freshbundeliveryapp.model.orderdata.deliveredorder.OrderDeliveredResponse
import com.witnovus.freshbundeliveryapp.model.orderdata.deliveredorderlist.DeliveredOrderListResponse
import com.witnovus.freshbundeliveryapp.model.orderdata.notdeliveredorder.OrderNotDeliveredResponse
import com.witnovus.freshbundeliveryapp.model.orderdata.ongoingorderlist.OrderListResponse
import com.witnovus.freshbundeliveryapp.model.orderdata.vacationmode.vacationapply.VacationApplyResponse
import com.witnovus.freshbundeliveryapp.model.orderdata.vacationmode.vacationverify.VacationVerifyResponse
import com.witnovus.freshbundeliveryapp.utils.api.APIResponse
import com.witnovus.freshbundeliveryapp.utils.api.AppResult
import com.witnovus.freshbundeliveryapp.utils.api.ErrorData
import kotlinx.coroutines.launch

class OrderDetailViewModel(private val repository: OrderDetailRepository): ViewModel() {

    private val mLoadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = mLoadingState

    val getOrderListDetailData = MutableLiveData<OrderListResponse>()
    val orderDeliveredData = MutableLiveData<OrderDeliveredResponse>()
    var getdeliveredOrderListData = MutableLiveData<DeliveredOrderListResponse>()
    var notDeliveredOrderData = MutableLiveData<OrderNotDeliveredResponse>()
    var vacationApplyData = MutableLiveData<VacationApplyResponse>()
    var vacationVerifyData = MutableLiveData<VacationVerifyResponse>()

    fun getOrderListDetailResponse(getOrderListDetailRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.getOrderListDetail(getOrderListDetailRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val getOrderListDetailDataResponse = gson.fromJson<OrderListResponse>(
                                responseJson,
                                OrderListResponse::class.java
                            )
                            getOrderListDetailData.value = getOrderListDetailDataResponse
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

    fun orderDeliveredResponse(orderDeliveredRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.orderDelivered(orderDeliveredRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val orderDeliveredDataResponse = gson.fromJson<OrderDeliveredResponse>(
                                responseJson,
                                OrderDeliveredResponse::class.java
                            )
                            orderDeliveredData.value = orderDeliveredDataResponse
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

    fun deliveredOrderListResponse(deliveredOrderListRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.deliveredOrderList(deliveredOrderListRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val getdeliveredOrderListDataResponse = gson.fromJson<DeliveredOrderListResponse>(
                                responseJson,
                                DeliveredOrderListResponse::class.java
                            )
                            getdeliveredOrderListData.value = getdeliveredOrderListDataResponse
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

    fun notDeliveredOrderResponse(notDeliveredOrderRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.notDeliveredOrder(notDeliveredOrderRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val notDeliveredOrderDataResponse = gson.fromJson<OrderNotDeliveredResponse>(
                                responseJson,
                                OrderNotDeliveredResponse::class.java
                            )
                            notDeliveredOrderData.value = notDeliveredOrderDataResponse
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

    fun vacationApply(vacationApplyRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.vacationApply(vacationApplyRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val vacationApplyDataResponse = gson.fromJson<VacationApplyResponse>(
                                responseJson,
                                VacationApplyResponse::class.java
                            )
                            vacationApplyData.value = vacationApplyDataResponse
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

    fun vacationVerify(vacationVerifyRequestHashmap: String) {
        mLoadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            val result = repository.vacationVerify(vacationVerifyRequestHashmap)
            when (result) {
                is AppResult.Success -> {
                    mLoadingState.postValue(LoadingState.LOADED)
                    result.successData.let {
                        val gson = GsonBuilder().create()
                        val responseJson = gson.toJson(it)
                        val responseData =
                            gson.fromJson<APIResponse>(responseJson, APIResponse::class.java)
                        if (responseData.success) {
                            val vacationVerifyDataResponse = gson.fromJson<VacationVerifyResponse>(
                                responseJson,
                                VacationVerifyResponse::class.java
                            )
                            vacationVerifyData.value = vacationVerifyDataResponse
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