package com.witnovus.freshbundeliveryapp.utils.api


import com.google.gson.GsonBuilder
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object ApiErrorUtils {

    fun parseError(response: Response<*>): ErrorData {
        val gson = GsonBuilder().create()
        var error: ErrorData
        try {
//            val errorJsonObject = gson.toJson(response.errorBody()?.string())

            /*val mainJson: JSONObject = response as JSONObject
            val dynamicJson: JSONObject = mainJson.getJSONObject("data")
            val keys: Iterator<*> = dynamicJson.getJSONObject("data").keys()

            var errorJson = JSONObject() // will return key value of errors

            var errorStrings: String = ""
            while (keys.hasNext()) { // loop to get the dynamic key
                val currentDynamicKey = keys.next() as String
                // get the value of the dynamic key
                val currentDynamicArray: JSONArray = dynamicJson.getJSONArray(currentDynamicKey)
                // do something here with the value... or either make another while loop to Iterate further
                for (i in 0 until currentDynamicArray.length()) {
                    errorStrings += currentDynamicArray.getString(i) + "\n"
                }
                errorJson.put(currentDynamicKey, errorStrings)
            }
            ////Log.d("JSON Value", errorJson.toString())*/

//            error = gson.fromJson(errorJson.toString(), APIError::class.java)

//            println(">>>>>>>>>>>>>>. response body: ${response.body()}")
//            println(">>>>>>>>>>>>>>. response errorBody: ${response.errorBody()}")

//            val gson = Gson()
//            val type = object : TypeToken<ErrorData>() {}.type
//            var errorResponse: ErrorData? = gson.fromJson(response.errorBody()!!.charStream(), type)
//            println(">>>>>>>>>>>>>>. errorResponse: ${errorResponse?.message}")
//            println(">>>>>>>>>>>>>>. errorResponse: ${errorResponse?.data}")
//            println(">>>>>>>>>>>>>>. errorResponse: ${errorResponse?.errorCode}")
//            println(">>>>>>>>>>>>>>. errorResponse: ${errorResponse?.success}")

//          val errorData = gson.fromJson(response.errorBody()?.string(), ErrorData::class.java)

            val errorData = gson.fromJson(response.errorBody()?.string(), ErrorData::class.java)
            var errorMessage = errorData.message
            var errorCode = errorData.errorCode
            error = ErrorData(null, errorCode, errorMessage, false)

        } catch (e: IOException) {
            e.message?.let { ////Log.d(TAG, it)
                 }
            var errorMessage =e.message
            var errorCode = 1000
            error = ErrorData(null, errorCode, errorMessage!!, false)

            return error
        }
        return error
    }
}