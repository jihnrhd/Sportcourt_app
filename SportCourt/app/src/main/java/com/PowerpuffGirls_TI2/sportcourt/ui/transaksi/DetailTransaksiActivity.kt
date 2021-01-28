package com.PowerpuffGirls_TI2.sportcourt.ui.transaksi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityDetailTransaksiBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Transaksi
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat

class DetailTransaksiActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_TRANSACTION = "extra_transaction"
    }

    private lateinit var binding: ActivityDetailTransaksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTitle()
        val transaksi = intent.getParcelableExtra<Transaksi>(EXTRA_TRANSACTION)
        if (transaksi != null) getData(transaksi)


    }

    private fun initTitle() {
        setSupportActionBar(binding.toolbar)
        binding.collapsingToolbar.title = "Detail Transaksi"
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

    private fun getData(transaksi: Transaksi) {
        binding.tvNamaLapangan.text = transaksi.nama
        binding.tvTanggalPinjam.text = transaksi.waktu
        binding.tvDetailLapangan.text = transaksi.detail
        binding.tvNamaPeminjam.text = transaksi.namaPeminjam
        binding.tvAlamatPeminjam.text = transaksi.alamatPeminjam
        val price = transaksi.harga?.toInt()
        binding.tvPrice.text = price?.let { PriceFormat.rupiah(it) }
        binding.tvStatusTransaksi.text = transaksi.status
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}