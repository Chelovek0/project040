package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val userEmail: EditText = findViewById(R.id.email_si)
        val userPass: EditText = findViewById(R.id.pass_si)
        val move: TextView = findViewById(R.id.link_text)
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
                user.sendPostAuth(email, pass)
            }
        }
    }
}