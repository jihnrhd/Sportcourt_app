package com.PowerpuffGirls_TI2.sportcourt.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.PowerpuffGirls_TI2.sportcourt.adapter.KeranjangAdapter
import com.PowerpuffGirls_TI2.sportcourt.databinding.FragmentKeranjangBinding
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganEntity
import com.PowerpuffGirls_TI2.sportcourt.local.LapanganRoomDatabase
import com.PowerpuffGirls_TI2.sportcourt.utils.UsersID


class KeranjangFragment : Fragment() {
    private lateinit var binding: FragmentKeranjangBinding
    private lateinit var keranjangAdapter: KeranjangAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKeranjangBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keranjangAdapter = KeranjangAdapter()
        getCart()
        with(binding.rvLapangan) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = keranjangAdapter
        }
    }

    private fun getCart() {
        val list: List<LapanganEntity> =
            LapanganRoomDatabase.getDatabase(requireContext()).lapanganDao()
                .getAllLapangan(UsersID.userID)
        if (list.isNotEmpty()) {
            binding.tvInformation.visibility = View.GONE
            binding.rvLapangan.visibility = View.VISIBLE
            binding.lottieInfo.visibility = View.GONE
            keranjangAdapter.setData(list)
        } else {
            binding.lottieInfo.visibility = View.VISIBLE
            binding.rvLapangan.visibility = View.GONE
            binding.tvInformation.visibility = View.VISIBLE
            binding.tvInformation.text = "Daftar keranjang masih kosong"
        }
    }

    override fun onResume() {
        super.onResume()
        getCart()
    }
}