package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userEmail: EditText = findViewById(R.id.email_reg)
        val userPass1: EditText = findViewById(R.id.pass1_reg)
        val userPass2: EditText = findViewById(R.id.pass2_reg)
        val buttonReg: Button = findViewById(R.id.button_reg)

        buttonReg.setOnClickListener {

            val email = userEmail.text.toString().trim()
            val pass1 = userPass1.text.toString().trim()
            val pass2 = userPass2.text.toString().trim()

            if(email == "" || pass1 == "" || pass2 == "") {
                Toast.makeText(this, "Заполните пустые поля!", Toast.LENGTH_LONG).show()
            } else if (pass1 != pass2) {
                Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_LONG).show()
            } else {
                val user = User(email, pass1)
                user.sendPostRequest(email, pass1)
            }
        }


    }
}