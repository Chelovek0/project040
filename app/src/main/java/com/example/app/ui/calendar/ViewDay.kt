package com.example.app.ui.today

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import com.google.gson.JsonParser
import java.io.IOException
import java.io.StringReader
import com.google.gson.stream.JsonReader

class ViewDay(val hash_val: String?, val nowDate: String) {
    fun getDate(hash_val: String?, nowDate: String, callback: (Array<String>) -> Unit) {
        val url = "http://platwa-server.ru:32673/info/get"
        val client = OkHttpClient()

        val js_date = JSONObject().apply {
            put("hash_auth", hash_val)
            put("date", nowDate)
        }

        val requestBody: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), js_date.toString())

        val request: Request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val resp = arrayOf("8")
                println(e.message)
                callback(resp)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()?.let { String(it.toByteArray(Charsets.UTF_8)) }
                if (responseBody != null) {
                    println("Response body: $responseBody")
                    try {
                        val jsonReader = JsonReader(StringReader(responseBody))
                        jsonReader.isLenient = true
                        val jsonElement = JsonParser.parseReader(jsonReader).asJsonObject
                        val gen = jsonElement.get("mood").asString
                        val weather = jsonElement.get("weather").asString
                        val place = jsonElement.get("whereiam").asString
                        val with = jsonElement.get("whom").asString
                        val thinks = jsonElement.get("comment").asString
                        val resp = arrayOf(gen, weather, place, with, thinks)
                        println("Gen ${resp[0]}")
                        println("Wea ${resp[1]}")
                        println("Pla ${resp[2]}")
                        println("With ${resp[3]}")
                        println("Thi ${resp[4]}")
                        callback(resp)
                    } catch (e: Exception) {
                        println("Error parsing JSON: ${e.message}")
                        callback(arrayOf("8"))
                    }
                } else {
                    println("Response body was null")
                    callback(arrayOf("8"))
                }
            }
        })
    }
}
