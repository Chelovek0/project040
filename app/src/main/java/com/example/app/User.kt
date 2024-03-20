package com.example.app


import okhttp3.*
import java.io.IOException
class User(val userName: String, val password: String) {
    fun sendPostReg(userName: String, password: String, callback: (String) -> Unit) {
        val url = "http://localhost:4444/register"
        val client = OkHttpClient()
        val requestBody: RequestBody = FormBody.Builder()
            .add("userName", userName)
            .add("password", password)
            .build()
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

