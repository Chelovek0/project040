package com.example.app

import okhttp3.OkHttpClient

class User(val userName: String, val password: String) {
    fun sendPostRequest(userName:String, password:String) {
        val url : String = "https://google.com"
        val client : OkHttpClient = OkHttpClient()

        // Дальше надо реализовать post запрос и передать данные. Этот запрос
        // Выдаст тебе json, в котором будет инфа
    }
}