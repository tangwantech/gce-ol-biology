package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.AppData
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class RestRepository {
    companion object{
        const val SIGN_UP = "signup"
        const val GET_APP_DATA = "getAppData"
        const val GET_APP_VERSION = "getAppVersion"
        const val UPDATE_SUBSCRIPTION = "updateSubscription"
        const val GET_PACKAGE_TYPES = "getPackageTypes"
        const val GET_CAMPAY_CREDENTIALS = "getCampayCredentials"
//        const val GET_SUBSCRIPTION = "getSubscription"
//        const val GET_USER_DATA = "getUserData"
        const val APPLICATION_ID = "9W3lYkeIpf3JWrjroH4bE9mL5HPC9ZFybVdLgOLn"
        const val CLIENT_KEY = "wPNTqR18DRbZr7UK8jfaIQItw7t7q9xiUX96AWMe"
    }

    fun query(queryType:String, params: HashMap<String, String>, listener: OnQueryListener){
        val client: OkHttpClient = OkHttpClient.Builder().build()
        val url = when (queryType){
            SIGN_UP -> "https://parseapi.back4app.com/functions/signup"
            GET_APP_DATA -> "https://parseapi.back4app.com/functions/getAppData"
            UPDATE_SUBSCRIPTION -> "https://parseapi.back4app.com/functions/updateSubscription"
            GET_PACKAGE_TYPES -> "https://parseapi.back4app.com/functions/getPackageTypes"
            GET_CAMPAY_CREDENTIALS -> "https://parseapi.back4app.com/functions/getCampayCredentials"
            else -> "https://parseapi.back4app.com/functions/getAppVersion"
        }


        val mediaType = "application/json;charset=utf-8".toMediaType()
        val requestBody = RequestBody.create(mediaType, JSONObject(params as Map<*, *>).toString())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Parse-Application-Id", APPLICATION_ID)
            .addHeader("X-Parse-REST-API-Key", CLIENT_KEY)
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
//                    e.localizedMessage?.let { listener.onError(it)}
                println("error withing enqueue...${e.localizedMessage?.toString()}")
                call.cancel()
                listener.onError(e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string().toString()
                val json = JSONObject(responseBody)

                if (json.has("error")){
                    println("error....$json")
                    listener.onError(json.getString("error").toString())
                }else{
                    listener.onSuccess(json.getString("result").toString())
                }

            }
        })

    }

    interface OnQueryListener{
        fun onSuccess(result: String)
        fun onError(error: String?)

    }
}