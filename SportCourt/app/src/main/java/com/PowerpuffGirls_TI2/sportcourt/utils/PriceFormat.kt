package com.PowerpuffGirls_TI2.sportcourt.utils

import java.text.NumberFormat
import java.util.*

class PriceFormat { companion object {
    fun rupiah(number: Int): String{
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number).toString()
    }
}
}