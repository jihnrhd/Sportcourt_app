package com.PowerpuffGirls_TI2.sportcourt.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class LapanganEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "nama")
    var nama: String,

    @ColumnInfo(name = "id_peminjam")
    var id_peminjam: String,

    @ColumnInfo(name = "nama_peminjam")
    var namaPeminjam: String,

    @ColumnInfo(name = "alamat_peminjam")
    var alamatPeminjam: String,

    @ColumnInfo(name = "detail")
    var detail: String,

    @ColumnInfo(name = "gambar")
    var gambar: String,

    @ColumnInfo(name = "harga")
    var harga: String,

    @ColumnInfo(name = "waktu")
    var waktu: String,

    @ColumnInfo(name = "status")
    var status: String,

    ) : Parcelable