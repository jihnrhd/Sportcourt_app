package com.PowerpuffGirls_TI2.sportcourt.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.PowerpuffGirls_TI2.sportcourt.DetailActivity
import com.PowerpuffGirls_TI2.sportcourt.MyData
import com.PowerpuffGirls_TI2.sportcourt.R

class cardview_areapeminjaman(private val listMyDatas: ArrayList<MyData>,
                              val context: Context
):
    RecyclerView.Adapter<cardview_areapeminjaman.CardViewViewHolder>() {
    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDetail: TextView = itemView.findViewById(R.id.tv_item_detail)
        var btnFavorite: Button = itemView.findViewById(R.id.btn_set_Favorite)
        var btnOrder : Button = itemView.findViewById(R.id.btn_set_Order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_areapeminjaman, parent, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMyDatas.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        val myData = listMyDatas[position]
        Glide.with(holder.itemView.context)
            .load(myData.photo)
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgPhoto)
        holder.tvName.text = myData.name
        holder.tvDetail.text = myData.description
        holder.btnFavorite.setOnClickListener {
            Toast.makeText(holder.itemView.context,
                "Favorite " + listMyDatas[holder.adapterPosition].name, Toast.LENGTH_SHORT).show()
        }
        holder.btnOrder.setOnClickListener {
            Toast.makeText(holder.itemView.context,
                "Order " + listMyDatas[holder.adapterPosition].name, Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener{
            val moveWithObjectIntent = Intent(context, DetailActivity::class.java)
            moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MYDATA, myData)
           context.startActivity(moveWithObjectIntent)
        }
    }



}