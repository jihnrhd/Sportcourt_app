package com.PowerpuffGirls_TI2.sportcourt.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ActivityDetailBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Lapangan
import com.PowerpuffGirls_TI2.sportcourt.ui.booking.BookingActivity
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LAPANGAN = "extra_lapangan"
    }

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extraLapangan = intent.getParcelableExtra<Lapangan>(EXTRA_LAPANGAN)
        if (extraLapangan != null) {
            binding.contentDetail.tvNameDesc.text = extraLapangan.nama.toString()
            binding.contentDetail.tvDetailDesc.text = extraLapangan.detail.toString()
            Glide.with(this@DetailActivity)
                .load(extraLapangan.gambar)
                .into(binding.ivBackDrop)
            setCollapsing(extraLapangan.nama.toString())
            val price = extraLapangan.harga?.toInt()
            binding.tvPriceDetail.text = price?.let { PriceFormat.rupiah(it) }

        }

        binding.contentDetail.tvLocDetail.setOnClickListener {
            if (extraLapangan != null) {
                val intent = Intent(this, MapsDetailActivity::class.java)
                intent.putExtra(MapsDetailActivity.EXTRA_LAPANGAN, extraLapangan)
                startActivity(intent)
            }

        }

        binding.btnBooking.setOnClickListener {
            intent = Intent(this@DetailActivity, BookingActivity::class.java)
            intent.putExtra(BookingActivity.EXTRA_BOOKING, extraLapangan)
            startActivity(intent)
        }


    }

    private fun setCollapsing(title: String) {
        binding.collapsingToolbar.title = ""
        binding.tvTitle.text = " "
        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )

        binding.appbar.setExpanded(true)
        val mAppBarLayout = findViewById<View>(R.id.appbar) as AppBarLayout
        mAppBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1


            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.title = title
                    binding.tvTitle.text = title
                    isShow = true
                } else if (isShow) {
                    binding.collapsingToolbar.title = " "
                    binding.tvTitle.text = " "
                    isShow = false
                }
            }

        })

    }




    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}