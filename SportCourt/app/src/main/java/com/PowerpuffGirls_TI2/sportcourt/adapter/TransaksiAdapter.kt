package com.PowerpuffGirls_TI2.sportcourt.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.databinding.ItemTransactionBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Transaksi
import com.PowerpuffGirls_TI2.sportcourt.ui.transaksi.DetailTransaksiActivity
import com.PowerpuffGirls_TI2.sportcourt.utils.PriceFormat
import com.bumptech.glide.Glide

class TransaksiAdapter : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    private val listData = ArrayList<Transaksi>()

    fun setData(list: List<Transaksi>?) {
        if (list == null) return
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        holder.bindItem(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    class TransaksiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTransactionBinding.bind(itemView)
        fun bindItem(transaksi: Transaksi) {
            with(itemView) {
                binding.tvStatusItem.text = transaksi.status
                binding.tvNamaLapangan.text = transaksi.nama
                val price = transaksi.harga?.toInt()
                binding.tvPriceTransaksi.text = price?.let { PriceFormat.rupiah(it) }

                Glide.with(context)
                    .load(transaksi.gambar)
                    .into(binding.imgItem)
                setOnClickListener{
                    val intent = Intent(context,DetailTransaksiActivity::class.java)
                    intent.putExtra(DetailTransaksiActivity.EXTRA_TRANSACTION,transaksi)
                    context.startActivity(intent)
                }
            }
        }
    }
}