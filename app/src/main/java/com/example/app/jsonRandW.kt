package com.example.app

import java.io.File
import com.google.gson.Gson
import com.google.gson.GsonBuilder



class jsonRandW {
    fun writeJson(hash_auth: String){
        val gson = GsonBuilder().setPrettyPrinting().create()
        val hash = "{hash_auth: ${hash_auth}}"
        val data: String = gson.toJson(hash)
        File("data.json").writeText(data)
    }
    fun readJson(dat: String, callback: (String) -> Unit){
        val jsonFile = File("data.json")
        val jsonString = jsonFile.readText()
        val gson = Gson()
        val data:User = gson.fromJson(jsonString, User::class.java)
        callback(data.hash_auth) //Остановился тут, пока не пробовал особо исправить, и не очень понимаю как, возможно хрень творю
    }
}