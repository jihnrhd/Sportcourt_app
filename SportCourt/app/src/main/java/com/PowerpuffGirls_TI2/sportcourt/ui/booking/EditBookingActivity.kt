package com.PowerpuffGirls_TI2.sportcourt.ui.booking

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityEditBookingBinding
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganEntity
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganRoomDatabase
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class EditBookingActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_BOOKING = "Extra_booking"
        const val RESULT_CODE = 110
    }

    private lateinit var binding: ActivityEditBookingBinding
    private lateinit var  extraBooking: LapanganEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Edit Booking Lapangan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        extraBooking = intent.getParcelableExtra(EXTRA_BOOKING)!!
        setData(extraBooking)

        initClick()


    }

    private fun initClick() {
        binding.edtTanggalPinjam.setOnClickListener {
            setCalendar()
        }

        binding.btnBooking.setOnClickListener {
            checkData()
        }
    }

    private fun setData(extraBooking: LapanganEntity) {
        binding.edtName.setText(extraBooking.namaPeminjam)
        binding.edtAddress.setText(extraBooking.alamatPeminjam)
        binding.edtNamaLapangan.setText(extraBooking.nama)
        binding.edtTanggalPinjam.setText(extraBooking.waktu)
        val price = extraBooking.harga.toInt()
        binding.tvAmount.text = PriceFormat.rupiah(price)
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendar() {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        val picker = DatePickerDialog(
            this,
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
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val id = extraBooking.id
        val id_peminjam = userID
        val nama = extraBooking.nama
        val namaPeminjam = binding.edtName.text.toString()
        val detail = extraBooking.detail
        val gambar = extraBooking.gambar
        val harga = extraBooking.harga
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
                val lapanganEntity = LapanganEntity(
                    id,
                    nama,
                    id_peminjam,
                    namaPeminjam,
                    alamatPeminjam,
                    detail,
                    gambar,
                    harga,
                    waktu,
                    status
                )
                updateBooking(lapanganEntity)
            }
        }
    }

    private fun updateBooking(lapanganEntity: LapanganEntity) {
        LapanganRoomDatabase.getDatabase(this).lapanganDao().update(lapanganEntity)
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_BOOKING,lapanganEntity)
        setResult(RESULT_CODE, resultIntent)
        Toast.makeText(this,"Data berhasil diperbaharui", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}