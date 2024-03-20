package com.example.app

import okhttp3.OkHttpClient

class User(val userName: String, val password: String) {
    fun sendPostRequest(userName:String, password:String) {
        val url : String = "http://localhost:4444/register"
        val client : OkHttpClient = OkHttpClient()

        //Отправка данных  на сервер

    }
}