package com.PowerpuffGirls_TI2.sportcourt

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener{
            val username = inputUsername.text.toString()
            val password = inputPassword.text.toString()
            if (username.isEmpty()|| password.isEmpty()){
                Toast.makeText(this, "Harap Masukkan Username and Password", Toast.LENGTH_SHORT).show()
            }
            if(username == "admin" || password == "admin"){
                val progressDialog = ProgressDialog(this,
                    R.style.Theme_MaterialComponents_Light_Dialog)
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Loading...")
                progressDialog.show()
                val intent = Intent (this,UtamaActivity::class.java)
                startActivity(intent)
            }
        }
        tvRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}