package com.PowerpuffGirls_TI2.sportcourt.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lapangan(
    var id: String?,
    var nama: String?,
    var detail: String?,
    var gambar: String?,
    var harga: String?,
    var lat: String?,
    var lang: String?
) : Parcelable