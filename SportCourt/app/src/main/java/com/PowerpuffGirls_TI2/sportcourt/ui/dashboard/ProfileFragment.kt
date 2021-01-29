package com.PowerpuffGirls_TI2.sportcourt.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.PowerpuffGirls_TI2.sportcourt.databinding.FragmentProfileBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Users
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var users: Users


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        db = FirebaseFirestore.getInstance()
        binding.btnSimpan.setOnClickListener {
            checkData()
        }

    }

    private fun checkData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val namaPengguna = binding.edtNamaPengguna.text.toString()
        when {
            binding.edtNamaPengguna.text!!.isEmpty() -> {
                binding.edtNamaPengguna.error = "Tidak boleh Kosong"
            }
            else -> {
                val usersUpdate = hashMapOf(
                    "id" to userID,
                    "username" to namaPengguna,
                    "email" to users.email,
                    "image_url" to users.image_url
                )

                db.collection("users").document(userID)
                    .set(usersUpdate)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Berhasil disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e: Exception ->
                        Toast.makeText(
                            context,
                            "Registrasi Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }
    }


    private fun getData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("id", userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        users = Users(
                            document.getString("id"),
                            document.getString("email"),
                            document.getString("image_url"),
                            document.getString("username"),
                        )

                        binding.edtNamaPengguna.setText(users.username)
                        Glide.with(this)
                            .load(users.image_url)
                            .into(binding.civProfil)
                    }
                } else {
                    Log.d("ProfileFragment", "Error getting documents.", task.exception)
                }
            }


    }
}