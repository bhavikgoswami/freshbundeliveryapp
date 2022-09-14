package com.greypixstudio.broovisdeliveryapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UploadDocumentAPIInterface {

    //uploadLicense
    @Multipart
    @POST("delivery_user/document/add")
    suspend fun addLicenseDetail(@HeaderMap headers: Map<String ,String>,@Part("document_type") documentType: RequestBody?,
                                  @Part("licence_type") licenceType: RequestBody?,
                                  @Part("name") deliveryUsername: RequestBody?,
                                  @Part("number") number: RequestBody?,
                                  @Part("licence_register_date") licenceRegisterDate: RequestBody?,
                                  @Part("licence_expiry_date") licenceExpiryDate: RequestBody?,
                                  @Part frontSideMultipart: MultipartBody.Part?,
                                  @Part("front_side") frontSideRequestBody: RequestBody?,
                                  @Part backSideMultiplePart: MultipartBody.Part?,
                                  @Part("back_side") backSideRequestBody: RequestBody?): Response<Any>

    //get Licence
    @Headers("Content-Type: application/json")
    @POST("delivery_user/document/get")
    suspend fun getLicenseDetail(@HeaderMap headers: Map<String ,String>, @Body getLicenseDetailRequestHashmap :  String): Response<Any>


    //update licence
    @Multipart
    @POST("delivery_user/document/update")
    suspend fun updateLicenseDetail(@HeaderMap headers: Map<String ,String>,@Part("document_type") documentType: RequestBody?,
                                    @Part("licence_type") licenceType: RequestBody?,
                                    @Part("name") deliveryUsername: RequestBody?,
                                    @Part("number") number: RequestBody?,
                                    @Part("licence_register_date") licenceRegisterDate: RequestBody?,
                                    @Part("licence_expiry_date") licenceExpiryDate: RequestBody?,
                                    @Part frontSideMultipart: MultipartBody.Part?,
                                    @Part("front_side") frontSideRequestBody: RequestBody?,
                                    @Part backSideMultiplePart: MultipartBody.Part?,
                                    @Part("back_side") backSideRequestBody: RequestBody?): Response<Any>
    //get AdharCard
    @Headers("Content-Type: application/json")
    @POST("delivery_user/document/get")
    suspend fun getAdharCardDetail(@HeaderMap headers: Map<String ,String>, @Body getAdharDetailRequestHashmap :  String): Response<Any>

    //uploadAdharCard
    @Multipart
    @POST("delivery_user/document/add")
    suspend fun addAadhaarCardDetail(@HeaderMap headers: Map<String ,String>,
                                   @Part("document_type") documentType: RequestBody?,
                                   @Part("name") aadhaarCardUserName: RequestBody?,
                                   @Part("number") aadhaarCardNumber: RequestBody?,
                                   @Part frontSideMultipart: MultipartBody.Part?,
                                   @Part("front_side") frontSideRequestBody: RequestBody?,
                                   @Part backSideMultiplePart: MultipartBody.Part?,
                                   @Part("back_side") backSideRequestBody: RequestBody?): Response<Any>

    @Multipart
    @POST("delivery_user/document/update")
    suspend fun updateAadhaarCardDetail(@HeaderMap headers: Map<String ,String>,
                                     @Part("document_type") documentType: RequestBody?,
                                     @Part("name") aadhaarCardUserName: RequestBody?,
                                     @Part("number") aadhaarCardNumber: RequestBody?,
                                     @Part frontSideMultipart: MultipartBody.Part?,
                                     @Part("front_side") frontSideRequestBody: RequestBody?,
                                     @Part backSideMultiplePart: MultipartBody.Part?,
                                     @Part("back_side") backSideRequestBody: RequestBody?): Response<Any>

    //uploadPanCard
    @Multipart
    @POST("delivery_user/document/add")
    suspend fun addPanCardDetail(@HeaderMap headers: Map<String ,String>,
                                 @Part("document_type") documentType: RequestBody?,
                                 @Part("name") panCardUserName: RequestBody?,
                                 @Part("number") panCardNumber: RequestBody?,
                                 @Part frontSideMultipart: MultipartBody.Part?,
                                 @Part("front_side") frontSideRequestBody: RequestBody?): Response<Any>


    //bankList

    @Headers("Content-Type: application/json")
    @POST("delivery_user/bank/list")
    suspend fun bankListDetail(@HeaderMap headers: Map<String ,String>, @Body bankListDetailRequestHashmap :  String): Response<Any>

    //addBankDetail


    @Multipart
    @POST("delivery_user/bank/add")
    suspend fun addBankDetail(@HeaderMap headers: Map<String ,String>,
                              @Part("bank_id") bankId: RequestBody?,
                              @Part("account_name") bnakAccountUserName: RequestBody?,
                              @Part("account_no") accountNumber: RequestBody?,
                              @Part("account_type") accountType: RequestBody?,
                              @Part("branch_name") branchName: RequestBody?,
                              @Part("ifsc_code") ifscCode: RequestBody?,
                              @Part("micr_code") micrCode: RequestBody?,
                              @Part("branch_code") branchCode: RequestBody?,
                              @Part("bank_flag") bankFlag : RequestBody?,
                              @Part fileMultipart: MultipartBody.Part?,
                              @Part("file") imgRequestBody: RequestBody?): Response<Any>

    // get bank detail
    @Headers("Content-Type: application/json")
    @POST("delivery_user/bank/get")
    suspend fun getBankDetail(@HeaderMap headers: Map<String ,String>, @Body bankDetailRequestHashmap :  String): Response<Any>

    @Multipart
    @POST("delivery_user/bank/add")
    suspend fun updateBankDetail(@HeaderMap headers: Map<String ,String>,
                              @Part("bank_id") bankId: RequestBody?,
                              @Part("account_name") bnakAccountUserName: RequestBody?,
                              @Part("account_no") accountNumber: RequestBody?,
                              @Part("account_type") accountType: RequestBody?,
                              @Part("branch_name") branchName: RequestBody?,
                              @Part("ifsc_code") ifscCode: RequestBody?,
                              @Part("micr_code") micrCode: RequestBody?,
                              @Part("branch_code") branchCode: RequestBody?,
                              @Part fileMultipart: MultipartBody.Part?,
                              @Part("file") imgRequestBody: RequestBody?): Response<Any>

    // AddAddress

    @Headers("Content-Type: application/json")
    @POST("delivery_user/address/add")
    suspend fun addAddressDetail(@HeaderMap headers: Map<String ,String>, @Body addAddressDetailRequestHashmap :  String): Response<Any>

    // get Address Detail
    @Headers("Content-Type: application/json")
    @POST("delivery_user/address/get_single")
    suspend fun getAddressDetail(@HeaderMap headers: Map<String ,String>, @Body addressDetailRequestHashmap :  String): Response<Any>

    // update address Detail
    @Headers("Content-Type: application/json")
    @POST("delivery_user/address/update")
    suspend fun updateAddressDetail(@HeaderMap headers: Map<String ,String>, @Body addressDetailRequestHashmap :  String): Response<Any>


    //list of document

    @Headers("Content-Type: application/json")
    @POST("delivery_user/document/list")
    suspend fun documentListDetail(@HeaderMap headers: Map<String ,String>, @Body documentListDetailRequestHashmap :  String): Response<Any>

}