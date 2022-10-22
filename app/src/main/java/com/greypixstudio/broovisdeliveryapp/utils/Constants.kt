package com.greypixstudio.broovisdeliveryapp.utils

class Constants {
    companion object {
        const val IS_LOGIN = "is_login"
        const val DEVICE_TYPE_ID = "Android"
        //API URLs
        const val BASE_URL = "https://app.broovis.com/api/"
        const val SCREEN_WIDTH = "width"
        const val PREFIX_DOMAIN = "https://app.broovis.com/"
        const val BASE_URL_IMAGE = "https://app.broovis.com/"

/*
        const val TERMS_AND_CONDITION_URL = "http://broovis.com/"
        const val PRIVACY_POLICY_URL = "http://broovis.com/"
*/

        const val WEB_VIEW_URL = "url"
        const val SCREEN_TITLE = "title"

        // Request Parameter
        const val BEARER = "Bearer"
        const val AUTHORIZATION = "Authorization"
        const val USER_TOKEN = "token"
        const val USER_DETAILS = "userDetails"
        const val MALE = "Male"
        const val FEMALE = "Female"
        const val  DEVICE_TYPE = "Android"
        const val USER_MPIN = "mpin"

        const val NOTIFICATION_MSG = "msg"
        const val NOTIFICATION_BODY = "body"
        const val NOTIFICATION_TITLE = "title"
        const val NOTIFICATION_ORDER_ID = "order_id"
        const val NOTIFICATION_CHANNEL_ID = "122"

        const val NOTIFICATION_PROFILE_FIRST_REVIEW = "Profile first review!"
        const val NOTIFICATION_DOCUMENT_VERIFIED = "Document verified!"
        const val NOTIFICATION_PROFILE_APPROVED = "Profile approved!"


        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val RECORD = "record"
        const val ADDPERMANENTADDRESS = "addPermanentAddress"

        const val CUSTOM_EXCEPTION_ERROR_CODE = 1000
        const val INVALID_TOKEN_ERROR: Int = 401
        const val TOKEN_EXPIRED_ERROR: Int = 402
        const val ACCOUNT_DEACTIVATED_ERROR: Int = 403
        const val ERROR_MESSAGE="error_message"

        val LBL_MPIN="mpin"
        val BLANK_STRING=""
        val BLANK_SPACE =""
        const val DOCUMENT_TYPE = "document_type"
        const val DOCUMENT_TYPE_LICENCE = "Licence"
        const val DOCUMENT_TYPE_AADHAAR_CARD = "Aadhaar Card"
        const val UPLOAD_LICENSE = "licence"
        const val STATUS_PENDING = "Pending"
        const val STATUS_APPROVE = "Approve"

        const val ORDER_DELIVERED = "Delivered"
        const val ORDER_NOT_DELIVERED = "Not Delivered"
        const val CART_ORDER = "CART ORDER"
        const val TOPUP_ORDER = "TOPUP ORDER"
        const val SUBSCRIPTION_ORDER = "SUBSCRIPTION ORDER"
        const val ONE_TIME_BUYER = "One Time Buyer"
        const val SUBSCRIBER = "Subscriber"

        const val BANK_LIST_OBJECT = "bank_list_object"



        const val SUBSCRIPTION_ORDER_ID = "order_id"
        const val SUBSCRIPTION_ORDER_ID_VERIFY_VACATION_OTP = "subscription_order_id"
        const val VACATION_APPLY_START_DATE = "start_date"
        const val VACATION_APPLY_END_DATE = "end_date"

        const val FAQ_URL = "https://broovis.com"
        const val TERMS_AND_CONDITION_URL = "https://broovis.com/terms_condition.html"
        const val PRIVACY_POLICY_URL = "https://broovis.com/privacy_policy.html"
        const val ABOUT_US_URL = "https://broovis.com"
        const val SUPPORT_EMAIL = "contact@broovis.com"
        const val PHONE_NUMBER = "+919727555615"

        const val API_KEY = "AIzaSyA8FJNpD9BEz5BpzTM_wdb7dFYhE2xzprI"
        const val PROJECT_ID = "broovis-44102"
        const val APPLICATION_ID ="1:498352837148:android:0f2f0291d28089d4a5c5d9"

        const val CURRENT_ADDRESS_DATA = "currentAddressData"
        const val PERMANENT_ADDRESS_DATA = "permanentAddressData"

        const val CURRENT_ADDRESS_UPDATE_DATA = "currentAddressUpdateData"
        const val PERMANENT_ADDRESS_UPDATE_DATA = "permanentAddressUpdateData"

        const val ORDER_NUMBER ="ORDER_NUMBER"
        const val ORDER_TYPE = "ORDER_TYPE"
        const val CASH = "Cash"
        const val ONLINE = "Online"
        const val CASH_PAYMENT = "CASH_PAYMENT"

    }
}