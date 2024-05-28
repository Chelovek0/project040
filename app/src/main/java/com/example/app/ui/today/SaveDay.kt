package com.example.app.ui.today

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class SaveDay(val thinks: String, val gen: String, val weather: String, val place: String, val with: String, val nowDate:String) {
    fun sendDay(thinks: String, gen: String, weather: String, place: String, with: String, nowDate: String, callback: (Array<String>) -> Unit) {
        val url = "http://platwa-server.ru:32673/register"
        val client = OkHttpClient()

        val js_day = JSONObject()
        js_day.put("thinks", thinks)
        js_day.put("gen", gen)
        js_day.put("weather", weather)
        js_day.put("place", place)
        js_day.put("with", with)
        js_day.put("date", nowDate)

        val requestBody: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), js_day.toString())

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
    fun date(nowDate: String, callback: (String) -> Unit) {
        val url = "http://platwa-server.ru:32673/register"
        val client = OkHttpClient()

        val js_date = JSONObject()
        js_date.put("email", nowDate)

        val requestBody: RequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), js_date.toString())

        val request: Request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {


            }

            override fun onResponse(call: Call, response: Response) {
                val resp: String = response.body?.string() ?: "Empty response"
                if (resp == nowDate){
                    callback("1")
                } else {
                    callback("0")
                }
            }
        })
    }
}