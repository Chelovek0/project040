package com.example.app
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class User(val userName: String, val password: String) {
    fun sendCredentialsToServer() {
        val url = URL("http://localhost:4444/register")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            doOutput = true

            val postData = "username=${URLEncoder.encode(userName, "UTF-8")}&password=${URLEncoder.encode(password, "UTF-8")}"
            val wr = OutputStreamWriter(outputStream)
            wr.write(postData)
            wr.flush()

            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                println("Response : $response")
            }
        }
    }
}