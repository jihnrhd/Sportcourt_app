package com.PowerpuffGirls_TI2.sportcourt.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    var id: String?,
    var email: String?,
    var image_url: String?,
    val username: String?
) : Parcelable