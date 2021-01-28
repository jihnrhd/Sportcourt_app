package com.PowerpuffGirls_TI2.sportcourt.ui.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityRegisterBinding
import com.PowerpuffGirls_TI2.sportcourt.ui.dashboard.DashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this@RegisterActivity)
        progressDialog.setMessage("Tunggu sebentar")
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.buttonRegister.setOnClickListener {
            checkData()
        }

        binding.textViewLogin.setOnClickListener {
            finish()
        }
    }

    private fun checkData() {
        val username = binding.editTextUserName.text.toString()
        val password = binding.editTextPassword.text.toString()
        val email = binding.editTextEmail.text.toString()
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Harap Lengkapi Data Anda", Toast.LENGTH_SHORT).show()
        } else {
            register(username, email, password)
        }
    }

    private fun register(username: String, email: String, pw: String) {
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener(this@RegisterActivity) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser!!
                    val uid = firebaseUser.uid
                    val users = hashMapOf(
                        "id" to uid,
                        "username" to username,
                        "email" to email,
                        "image_url" to "https://i.pinimg.com/originals/d1/1a/45/d11a452f5ce6ab534e083cdc11e8035e.png"
                    )

                    db.collection("users").document(uid)
                        .set(users)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(this@RegisterActivity, DashboardActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }.addOnFailureListener { e: Exception ->
                            Log.d("Register", "Error writing document", e)
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi Gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    progressDialog.dismiss()
                    val message = "Email sudah terdaftar"
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}