package com.PowerpuffGirls_TI2.sportcourt

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityMainBinding
import com.PowerpuffGirls_TI2.sportcourt.ui.dashboard.DashboardActivity
import com.PowerpuffGirls_TI2.sportcourt.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(
            this,
            R.style.Theme_MaterialComponents_Light_Dialog
        )


        binding.btnLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString()
            val password = binding.inputPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap Masukkan Username and Password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                progressDialog.isIndeterminate = true
                progressDialog.setMessage("Loading...")
                progressDialog.show()
                login(username, password)
            }
        }
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, pw: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, pw)
            .addOnCompleteListener(this@MainActivity) { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                } else {
                    val message = "Email atau password salah"
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            val intent = Intent(this@MainActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}