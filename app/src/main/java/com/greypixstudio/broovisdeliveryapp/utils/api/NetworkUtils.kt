package com.greypixstudio.broovisdeliveryapp.utils.api

import com.greypixstudio.broovisdeliveryapp.utils.Constants
import io.paperdb.Paper

class NetworkUtils {
    companion object {
        /**
         * Get device token for API calls
         */
        fun getToken(): String {
//            return Paper.book().read<String>(Constants.USER_TOKEN)
            return Constants.BEARER.plus(" ").plus(Paper.book().read<String>(Constants.USER_TOKEN))
        }

//        fun getSelectedLanguage():String{
//            return Paper.book().read<String>(Constants.language,"en")
//        }

        /**
         * Get headers for API access.
         */
        fun getHeaders(): HashMap<String, String> {
            val header = HashMap<String, String>()
         header[Constants.AUTHORIZATION] = getToken()

            //header[Constants.AUTHORIZATION] ="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvYXBwLm5lcHplcC5jb21cL2FwaVwvZGVsaXZlcnlfdXNlclwvdmVyaWZ5LW90cCIsImlhdCI6MTY1MjcwNjY5MywiZXhwIjoxNjU1MzM0NjkzLCJuYmYiOjE2NTI3MDY2OTMsImp0aSI6InRJU2F1M3F2emZldDFzZEIiLCJzdWIiOjYsInBydiI6IjIyNzRkZmYzYjQ0NjU5NDI1OGJiNDA2YTA1MjAzNjUxMzgyNjUyYmIifQ.nol7ajsJ8sAbKso56TvaH1UU4g7LiUYvAhFVDhkEqGw"
            println("getHeaders: $header")
            return header
        }
    }
}