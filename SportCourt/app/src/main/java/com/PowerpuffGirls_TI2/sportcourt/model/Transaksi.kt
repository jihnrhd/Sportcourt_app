package com.PowerpuffGirls_TI2.sportcourt.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaksi(
    var id: String?,
    var id_peminjam: String?,
    var nama: String?,
    var namaPeminjam: String?,
    var alamatPeminjam: String?,
    var detail: String?,
    var gambar: String?,
    var harga: String?,
    var waktu: String?,
    var status: String?,
) : Parcelable