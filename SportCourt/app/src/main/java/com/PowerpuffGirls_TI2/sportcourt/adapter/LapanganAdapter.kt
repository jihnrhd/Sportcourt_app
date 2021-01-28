package com.PowerpuffGirls_TI2.sportcourt.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ItemLapanganBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Lapangan
import com.PowerpuffGirls_TI2.sportcourt.ui.detail.DetailActivity
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.bumptech.glide.Glide

class LapanganAdapter : RecyclerView.Adapter<LapanganAdapter.LapanganViewHolder>() {
    private val listData = ArrayList<Lapangan>()

    fun setData(list: List<Lapangan>?) {
        if (list == null) return
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LapanganViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lapangan, parent, false)
        return LapanganViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapanganViewHolder, position: Int) {
        holder.bindItem(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    class LapanganViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLapanganBinding.bind(itemView)
        fun bindItem(lapangan: Lapangan) {
            with(itemView) {
                binding.ivNameLapangan.text = lapangan.nama
                val price = lapangan.harga?.toInt()
                binding.tvHargaLapangan.text =
                    price?.let { PriceFormat.rupiah(it) }
                Glide.with(context)
                    .load(lapangan.gambar)
                    .into(binding.ivLapangan)
                binding.btnDetail.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_LAPANGAN, lapangan)
                    context.startActivity(intent)
                }
            }
        }
    }
}