package com.example.app

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class User(val userName: String, val password: String) {
    fun sendPostReg(userName: String, password: String, callback: (String) -> Unit) {
        val url = "http://platwa-server.ru:32673/register"
        val client = OkHttpClient()

        val json = JSONObject()
        json.put("email", userName)
        json.put("password", password)

        val requestBody: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

        val request: Request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData: String = response.body?.string() ?: "Empty response"
                callback(responseData)
            }
        })
    }
}