package com.PowerpuffGirls_TI2.sportcourt.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ItemKeranjangBinding
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganEntity
import com.PowerpuffGirls_TI2.sportcourt.ui.booking.DetailBookingActivity
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.bumptech.glide.Glide

class KeranjangAdapter : RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder>() {

    private val listData = ArrayList<LapanganEntity>()

    fun setData(list: List<LapanganEntity>?) {
        if (list == null) return
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KeranjangViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return KeranjangViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeranjangViewHolder, position: Int) {
        holder.bindItem(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    class KeranjangViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemKeranjangBinding.bind(itemView)
        fun bindItem(lapangan: LapanganEntity) {
            with(itemView) {
                binding.tvNameKeranjang.text = lapangan.nama
                binding.tvTglPinjamKeranjang.text = lapangan.waktu
                binding.tvItemDetailKeranjang.text = lapangan.detail

                val price = lapangan.harga.toInt()
                binding.tvPriceKeranjang.text = PriceFormat.rupiah(price)
                Glide.with(context)
                    .load(lapangan.gambar)
                    .into(binding.imgItem)
                setOnClickListener {
                    val intent = Intent(context, DetailBookingActivity::class.java)
                    intent.putExtra(DetailBookingActivity.EXTRA_BOOKING, lapangan)
                    context.startActivity(intent)
                }

            }
        }
    }
}