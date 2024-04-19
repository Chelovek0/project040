package com.example.app


import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class User(val userName: String, val password: String) {
    fun sendPostReg(userName: String, password: String, callback: (Array<String>) -> Unit) {
        val url = "http://platwa-server.ru:32673/register"
        val client = OkHttpClient()

        val js = JSONObject()
        js.put("email", userName)
        js.put("password", password)

        val requestBody: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), js.toString())

        val request: Request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val resp = arrayOf("${e.message}","0")
                callback(resp)

            }

            override fun onResponse(call: Call, response: Response) {
                val hash_auth: String = response.body?.string() ?: "Empty response"
                val resp = arrayOf(hash_auth,"1")
                callback(resp)
            }
        })
    }
    fun sendPostLog(userName: String, password: String, callback: (Array<String>) -> Unit) {
        val url = "http://platwa-server.ru:32673/login"
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
                val resp: Array<String> = arrayOf("${e.message}", "0")
                callback(resp)
            }

            override fun onResponse(call: Call, response: Response) {
                val hash_auth: String = response.body?.string() ?: "Empty response"

                val resp: Array<String> = arrayOf(hash_auth, "1")
                callback(resp)

            }
        })
    }
}