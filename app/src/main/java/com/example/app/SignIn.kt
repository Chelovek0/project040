package com.example.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.os.Looper

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        val userEmail: EditText = findViewById(R.id.email_si)
        val userPass: EditText = findViewById(R.id.pass_si)
        val move: TextView = findViewById(R.id.llink)
        val buttonSi: Button = findViewById(R.id.button_si)

        move.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        buttonSi.setOnClickListener {

            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(email == "" || pass == "") {
                Toast.makeText(this, "Заполните пустые поля!", Toast.LENGTH_LONG).show()
            } else {
                val user = User(email, pass)
                user.sendPostLog(email, pass) {response ->
                    val hash = response[0]
                    println("Response: $hash")
                    if(response[1] == "1"){

                        val hash_saver = this.getSharedPreferences("hash", Context.MODE_PRIVATE)
                        val saver = hash_saver.edit()
                        saver.putString("hash", response[0])
                        saver.apply()

                        val intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Для выхода нажмите дважды.", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}