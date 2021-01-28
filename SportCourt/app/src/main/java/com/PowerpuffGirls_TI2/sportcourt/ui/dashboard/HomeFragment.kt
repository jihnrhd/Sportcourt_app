package com.PowerpuffGirls_TI2.sportcourt.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.PowerpuffGirls_TI2.sportcourt.adapter.LapanganAdapter
import com.PowerpuffGirls_TI2.sportcourt.broadcast.NetworkChangeReceiver
import com.PowerpuffGirls_TI2.sportcourt.databinding.FragmentHomeBinding
import com.PowerpuffGirls_TI2.sportcourt.model.Lapangan
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), NetworkChangeReceiver.ConnectivityReceiverListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var lapanganAdapter: LapanganAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        lapanganAdapter = LapanganAdapter()

        with(binding.rvLapangan) {
            rvLapangan.layoutManager = LinearLayoutManager(
                this.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
//            layoutManager = LinearLayoutManager(context)
//            setHasFixedSize(true)
            adapter = lapanganAdapter
        }

    }

    private fun getData() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val listLapangan = ArrayList<Lapangan>()
        db.collection("data")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val lapangan = Lapangan(
                            document.getString("id"),
                            document.getString("nama"),
                            document.getString("detail"),
                            document.getString("gambar"),
                            document.getString("harga"),
                            document.getString("lat"),
                            document.getString("lang")
                        )
                        listLapangan.addAll(listOf(lapangan))
                        lapanganAdapter.setData(listLapangan)
                    }
                } else {
                    Log.d("HomeFragment", "Error getting documents.", task.exception)
                }
            }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            binding.lottieInfo.visibility = View.VISIBLE
            binding.tvInformation.visibility = View.VISIBLE
            binding.textView2.visibility = View.GONE
            binding.rvLapangan.visibility = View.GONE

        } else {

            binding.lottieInfo.visibility = View.GONE
            binding.tvInformation.visibility = View.GONE
            binding.textView2.visibility = View.VISIBLE
            binding.rvLapangan.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        NetworkChangeReceiver.connectivityReceiverListener = this
    }


}