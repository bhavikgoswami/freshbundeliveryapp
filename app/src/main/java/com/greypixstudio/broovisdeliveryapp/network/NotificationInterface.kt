package com.greypixstudio.broovisdeliveryapp.network

import retrofit2.Response
import retrofit2.http.*

interface NotificationInterface {

    /* get notificationList */
    @Headers("Content-Type: application/json")
    @POST("delivery_user/notification/list")
    suspend fun getNotification(@HeaderMap token: HashMap<String, String>): Response<Any>

    /*Notification CLear*/
    @Headers("Content-Type: application/json")
    @POST("delivery_user/notification/clear")
    suspend fun clearNotification(@HeaderMap token: HashMap<String, String>): Response<Any>

    /*Notification mark as read*/
    @FormUrlEncoded
    @POST("delivery_user/notification/update/read_status")
    suspend fun markAsRead(
        @HeaderMap token: HashMap<String, String>,
        @Field("id") id: String,
        @Field("read_status") readStatus: String
    ): Response<Any>
}