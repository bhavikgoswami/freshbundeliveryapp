package com.witnovus.freshbundeliveryapp.network

import retrofit2.Response
import retrofit2.http.*

interface OrderDetailApiInterface {

    //Delivery Order List With Counts
    @Headers("Content-Type: application/json")
    @POST("delivery_user/order/list")
    suspend fun getOrderListDetail(@HeaderMap headers: Map<String ,String>, @Body getOrderListDetailRequestHashmap :  String): Response<Any>


    //order delivered
    @Headers("Content-Type: application/json")
    @POST("delivery_user/order/delivered")
    suspend fun orderDelivered(@HeaderMap headers: Map<String ,String>, @Body orderDeliveredRequestHashmap :  String): Response<Any>

    // delivered order list
    @Headers("Content-Type: application/json")
    @POST("delivery_user/order/delivered/list")
    suspend fun deliveredOrderList(@HeaderMap headers: Map<String ,String>, @Body deliveredOrderListRequestHashmap :  String): Response<Any>

    //not delivered order
    @Headers("Content-Type: application/json")
    @POST("delivery_user/order/not/delivered")
    suspend fun notDeliveredOrder(@HeaderMap headers: Map<String ,String>, @Body notDeliveredOrderRequestHashmap :  String): Response<Any>


    // vacation apply

    @Headers("Content-Type: application/json")
    @POST("delivery_user/subscription/order/vacation/apply")
    suspend fun vacationApply(@HeaderMap headers: Map<String ,String>, @Body vacationApplyRequestHashmap :  String): Response<Any>


    // vacation verify

    @Headers("Content-Type: application/json")
    @POST("delivery_user/subscription/order/vacation/verify")
    suspend fun vacationVerify(@HeaderMap headers: Map<String ,String>, @Body vacationVerifyRequestHashmap :  String): Response<Any>

   /* @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("delivery_user/subscription/order/vacation/verify")
    suspend fun vacationVerify(@HeaderMap headers: Map<String ,String>, @Field("subscription_order_id") subscription_order_id: String,@Field("start_date") start_date: String,@Field("end_date") end_date: String,@Field("otp") otp: String): Response<Any>*/
}