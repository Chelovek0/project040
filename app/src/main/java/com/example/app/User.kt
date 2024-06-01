package com.example.app

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import java.io.StringReader

class User(val userName: String, val password: String) {
    private val client = OkHttpClient()
    private val JSON = "application/json".toMediaTypeOrNull()

    fun sendPostReg(userName: String, password: String, callback: (Array<String>) -> Unit) {
        val url = "http://platwa-server.ru:32673/register"

        val json = JSONObject().apply {
            put("email", userName)
            put("password", password)
        }

        val requestBody = RequestBody.create(JSON, json.toString())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(arrayOf(e.message ?: "Unknown error", "0"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback(arrayOf("Request failed with code: ${response.code}", "0"))
                    return
                }

                val hashAuth = response.body?.string() ?: "Empty response"
                val jsonReader = JsonReader(StringReader(hashAuth))
                jsonReader.isLenient = true
                val jsonElement = JsonParser.parseReader(jsonReader).asJsonObject
                val gen = jsonElement.get("hash_auth").asString
                callback(arrayOf(gen, "1"))
            }
        })
    }

    fun sendPostLog(userName: String, password: String, callback: (Array<String>) -> Unit) {
        val url = "http://platwa-server.ru:32673/login"

        val json = JSONObject().apply {
            put("email", userName)
            put("password", password)
        }

        val requestBody = RequestBody.create(JSON, json.toString())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(arrayOf(e.message ?: "Unknown error", "0"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback(arrayOf("Request failed with code: ${response.code}", "0"))
                    return
                }

                val hashAuth = response.body?.string() ?: "Empty response"
                val jsonReader = JsonReader(StringReader(hashAuth))
                jsonReader.isLenient = true
                val jsonElement = JsonParser.parseReader(jsonReader).asJsonObject
                val gen = jsonElement.get("hash_auth").asString
                callback(arrayOf(gen, "1"))
            }
        })
    }
}
