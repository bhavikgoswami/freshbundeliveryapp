package com.witnovus.freshbundeliveryapp.network

import retrofit2.Response
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {

    /* get notificationList */
    @Headers("Content-Type: application/json")
    @POST("delivery_user/notification/list")
    suspend fun getNotification(@HeaderMap token: HashMap<String, String>): Response<Any>

    /*Notification CLear*/
    @Headers("Content-Type: application/json")
    @POST("delivery_user/notification/clear")
    suspend fun clearNotification(@HeaderMap token: HashMap<String, String>): Response<Any>
}