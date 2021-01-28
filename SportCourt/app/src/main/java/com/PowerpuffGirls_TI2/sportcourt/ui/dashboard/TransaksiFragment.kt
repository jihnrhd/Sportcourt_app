package com.PowerpuffGirls_TI2.sportcourt.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.PowerpuffGirls_TI2.sportcourt.adapter.TransaksiAdapter
import com.PowerpuffGirls_TI2.sportcourt.databinding.FragmentTransaksiBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Transaksi
import com.PowerpuffGirls_TI2.sportcourt.utils.UsersID
import com.google.firebase.firestore.FirebaseFirestore


class TransaksiFragment : Fragment() {
    private lateinit var binding: FragmentTransaksiBinding
    private lateinit var transaksiAdapter: TransaksiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransaksiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        transaksiAdapter = TransaksiAdapter()
        getData()

        with(binding.rvTransaction) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = transaksiAdapter
        }

    }

    private fun getData() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val listTransaction = ArrayList<Transaksi>()
        db.collection("transaksi")
            .whereEqualTo("id_peminjam", UsersID.userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val transaksi = Transaksi(
                            document.getString("id"),
                            document.getString("id_peminjam"),
                            document.getString("nama"),
                            document.getString("namaPeminjam"),
                            document.getString("alamatPeminjam"),
                            document.getString("detail"),
                            document.getString("gambar"),
                            document.getString("harga"),
                            document.getString("waktu"),
                            document.getString("status")
                        )
                        listTransaction.addAll(listOf(transaksi))


                        if (listTransaction.isEmpty()) {
                            binding.lottieInfo.visibility = View.VISIBLE
                            binding.tvInformation.visibility = View.VISIBLE
                            binding.rvTransaction.visibility = View.GONE
                        } else {
                            binding.lottieInfo.visibility = View.GONE
                            binding.tvInformation.visibility = View.GONE
                            binding.rvTransaction.visibility = View.VISIBLE
                            transaksiAdapter.setData(listTransaction)
                        }
                    }
                } else {
                    Log.d("TransaksiFragment", "Error getting documents.", task.exception)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}