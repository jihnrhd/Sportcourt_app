package com.PowerpuffGirls_TI2.sportcourt.ui.booking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityBookingBinding
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganEntity
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganRoomDatabase
import com.PowerpuffGirls_TI2.sportcourt.model.Lapangan
import com.PowerpuffGirls_TI2.sportcourt.model.Users
import com.PowerpuffGirls_TI2.sportcourt.ui.dashboard.DashboardActivity
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_BOOKING = "Extra_booking"
    }

    private lateinit var binding: ActivityBookingBinding
    private lateinit var users: Users
    private lateinit var extraLapangan: Lapangan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Booking Lapangan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        getDataUser()
        getDataLapangan()
        binding.edtTanggalPinjam.setOnClickListener {
            setCalendar()
        }

        binding.btnBooking.setOnClickListener {
            checkData()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getDataUser() {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("id", id)
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

                        binding.edtName.setText(users.username.toString())

                    }
                } else {
                    Log.d("BookingActivity", "Error getting documents")
                }
            }
    }

    private fun getDataLapangan() {
        extraLapangan = intent.getParcelableExtra(EXTRA_BOOKING)!!
        binding.edtNamaLapangan.setText(extraLapangan.nama.toString())
        val price = extraLapangan.harga?.toInt()
        binding.tvAmount.text = price?.let { PriceFormat.rupiah(it) }
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendar() {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        val picker = DatePickerDialog(
            this@BookingActivity,
            { _: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                binding.edtTanggalPinjam.setText(
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year1
                )
            }, year, month, day
        )
        picker.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkData() {
        val id_peminjam = users.id
        val nama = extraLapangan.nama
        val namaPeminjam = binding.edtName.text.toString()
        val detail = extraLapangan.detail
        val gambar = extraLapangan.gambar
        val harga = extraLapangan.harga
        val waktu = binding.edtTanggalPinjam.text.toString()
        val alamatPeminjam = binding.edtAddress.text.toString()
        val status = "Belum Bayar"

        when {
            binding.edtName.text!!.isEmpty() -> {
                binding.edtName.error = "Tidak boleh Kosong"
            }
            binding.edtAddress.text!!.isEmpty() -> {
                binding.edtAddress.error = "Tidak boleh Kosong"
            }
            binding.edtNamaLapangan.text!!.isEmpty() -> {
                binding.edtNamaLapangan.error = "Tidak boleh Kosong"
            }
            binding.edtTanggalPinjam.text!!.isEmpty() -> {
                binding.edtTanggalPinjam.error = "Tidak boleh Kosong"
            }
            else -> {
                val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val date = format.format(Date())
                val lapanganEntity = LapanganEntity(
                    date,
                    nama!!,
                    id_peminjam!!,
                    namaPeminjam,
                    alamatPeminjam,
                    detail!!,
                    gambar!!,
                    harga!!,
                    waktu,
                    status
                )
                insertToCart(lapanganEntity)
            }
        }
    }


    private fun insertToCart(lapangan: LapanganEntity) {
        LapanganRoomDatabase.getDatabase(this).lapanganDao().insert(lapangan)
        val intent =
            Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Toast.makeText(
            this,
            "Booking berhasil",
            Toast.LENGTH_SHORT
        ).show()
    }
}