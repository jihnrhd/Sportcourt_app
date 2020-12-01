package com.PowerpuffGirls_TI2.sportcourt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister.setOnClickListener{
            val username = editTextUserName.text.toString()
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString()
            if (username.isEmpty()||password.isEmpty()||email.isEmpty()){
                Toast.makeText(this, "Harap Lengkapi Data Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                Toast.makeText(this, "Registrasi Anda Berhasil", Toast.LENGTH_SHORT).show()
            }
        }

        textViewLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}