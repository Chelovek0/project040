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
import androidx.appcompat.widget.AppCompatButton
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val userEmail: EditText = findViewById(R.id.email_reg)
        val userPass1: EditText = findViewById(R.id.pass1_reg)
        val userPass2: EditText = findViewById(R.id.pass2_reg)
        val buttonReg: AppCompatButton = findViewById(R.id.button_reg)
        val move: TextView = findViewById(R.id.link_text)

        move.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

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
                user.sendPostReg(email, pass1) { response ->
                    val hash = response[0]
                    println("Response: $hash")

                    if(response[1] == "1"){
                        // Сохраняем хэш в SharedPreferences
                        val hash_saver = this.getSharedPreferences("hash", Context.MODE_PRIVATE)
                        val saver = hash_saver.edit()
                        saver.putString("hash", response[0])
                        saver.apply()

                        // Переходим на MainActivity2
                        val intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                        finish() // Закрываем MainActivity, чтобы пользователь не мог вернуться назад
                    }
                }
            }

        }

    }

    // Метод для обработки двойного нажатия на кнопку "Назад"
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Для выхода нажмите дважды.", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}
