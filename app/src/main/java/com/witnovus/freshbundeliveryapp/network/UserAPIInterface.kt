package com.witnovus.freshbundeliveryapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPIInterface {
    //Generate OTP
    @FormUrlEncoded
    @POST("delivery_user/send-otp")
    suspend fun generateOTP(@Field("mobile") mobile: String): Response<Any>

    //Generate OTP during Registration.....
    @FormUrlEncoded
    @POST("delivery_user/verify-otp")
    suspend fun verifyOTP(@Field("mobile") mobile: String,@Field("otp") otp: String,@Field("type") type: Int): Response<Any>

    // Logout user
    @POST("delivery_user/logout")
    suspend fun logoutUser(@HeaderMap headers: Map<String, String>/*,@Field("") mField: String*/): Response<Any>

//    // Signup user
//    @FormUrlEncoded
//    @POST("delivery_user/signup")
//    suspend fun signupUser(@Field("type") type: String,@Field("first_name") first_name: String,@Field("last_name") last_name: String,@Field("primary_contact_no") primary_contact_no: String,@Field("email") email: String): Response<Any>

    // Update Firebase token
    @FormUrlEncoded
    @POST("delivery_user/firebase/token/update")
    suspend fun updateFirebaseToken(@HeaderMap headers: Map<String, String>, @Field("firebase_token") firebase_token: String): Response<Any>

    // Get user details
    @POST("delivery_user/profile/get")
    suspend fun getUserDetails(@HeaderMap headers: Map<String, String>/*,@Field("") mField: String*/): Response<Any>

    // user profile
    @Multipart
    @POST("delivery_user/signup")
    suspend fun signupUser(
        @Part("type") type: RequestBody?,
        @Part("first_name") first_name: RequestBody?,
        @Part("last_name") last_name: RequestBody?,
        @Part("primary_contact_no") primary_contact_no: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("birthdate") birthdate: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("device_type") device_type: RequestBody?,
        @Part("has_license") has_license: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part("image") name: RequestBody?
    ): Response<Any>

    // Update user profile

    @Multipart
    @POST("delivery_user/profile/update")
    suspend fun userDetailUpdate(
        @HeaderMap headers: Map<String, String>,
        @Part("first_name") first_name: RequestBody?,
        @Part("last_name") last_name: RequestBody?,
        @Part("primary_contact_no") primary_contact_no: RequestBody?,
        @Part("alternative_contact_no") alternative_contact_no: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("birthdate") birthdate: RequestBody?,
        @Part("no_of_family_member") no_of_family_member: RequestBody?,
        @Part("device_type") device_type: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part("image") name: RequestBody?
    ): Response<Any>
    //setMpin

    @Headers("Content-Type: application/json")
    @POST("delivery_user/set/mpin")
    suspend fun setMpin(@HeaderMap headers: Map<String ,String>,@Body setMpinRequestHashmap :  String): Response<Any>

    //verify Mpin

    @Headers("Content-Type: application/json")
    @POST("delivery_user/login")
    suspend fun verifyMpin(@HeaderMap headers: Map<String ,String>,@Body verifyMpinRequestHashmap :  String): Response<Any>

    //Logout
    @Headers("Content-Type: application/json")
    @POST("delivery_user/logout")
    suspend fun logout(@HeaderMap headers: Map<String ,String>): Response<Any>

}