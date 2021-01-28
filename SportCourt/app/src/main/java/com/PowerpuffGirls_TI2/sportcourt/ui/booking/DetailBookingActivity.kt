package com.PowerpuffGirls_TI2.sportcourt.ui.booking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityDetailBookingBinding
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganEntity
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganRoomDatabase
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.google.firebase.firestore.FirebaseFirestore

class DetailBookingActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BOOKING = "extra booking"
        const val REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityDetailBookingBinding
    private lateinit var extraBooking: LapanganEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTitle()

        extraBooking = intent.getParcelableExtra(EXTRA_BOOKING)!!
        getData(extraBooking)

        binding.fabProfileEdit.setOnClickListener {
            val intent = Intent(this, EditBookingActivity::class.java)
            intent.putExtra(EditBookingActivity.EXTRA_BOOKING, extraBooking)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.btnBooking.setOnClickListener {
            showDialog(extraBooking)
        }

    }


    private fun showDialog(extraBooking: LapanganEntity?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Peringatan")
        builder.setMessage("Anda ingin melakukan pembayaran ?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            updateTransaction(extraBooking)
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }


        builder.show()
    }

    private fun showDeleteDialog(extraBooking: LapanganEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Peringatan")
        builder.setMessage("Anda ingin menghapus keranjang ?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            deleteCart(extraBooking)
            Toast.makeText(this, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
            finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }


        builder.show()
    }

    private fun updateTransaction(extraBooking: LapanganEntity?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        extraBooking?.status = "Diproses"
        if (extraBooking != null) {
            db.collection("transaksi").document()
                .set(extraBooking)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Pembayaran diproses",
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteCart(extraBooking)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Kesalahan $it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun deleteCart(extraBooking: LapanganEntity) {
        LapanganRoomDatabase.getDatabase(this).lapanganDao().delete(extraBooking)
    }

    private fun initTitle() {
        setSupportActionBar(binding.toolbar)
        binding.collapsingToolbar.title = "Detail Booking"
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(this, R.color.white)
        )
        binding.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(this, R.color.white)
        )
        if (supportActionBar != null) {
            supportActionBar!!.title = "Detail Booking"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun getData(booking: LapanganEntity) {
        binding.tvNamaLapangan.text = booking.nama
        binding.tvTanggalPinjam.text = booking.waktu
        binding.tvDetailLapangan.text = booking.detail
        binding.tvNamaPeminjam.text = booking.namaPeminjam
        binding.tvAlamatPeminjam.text = booking.alamatPeminjam
        val price = booking.harga.toInt()
        binding.tvPrice.text = PriceFormat.rupiah(price)
        binding.tvStatusTransaksi.text = booking.status
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == EditBookingActivity.RESULT_CODE) {
                val extraBooking =
                    data?.getParcelableExtra<LapanganEntity>(EditBookingActivity.EXTRA_BOOKING)
                if (extraBooking != null) {
                    getData(extraBooking)
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            showDeleteDialog(extraBooking)
        }
        return super.onOptionsItemSelected(item)

    }
}