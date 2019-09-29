package br.com.natanaelribeiro.basefeature.utils

import android.util.Patterns
import androidx.core.util.PatternsCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Double.toFormattedPrice(): String {

    val format = DecimalFormat.getInstance(Locale("pt", "BR"))

    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2

    return "R$ " + format.format(this)
}

fun Long.toFormattedDate(format: String): String {

    return try {
        val currentDate = Date(this)

        val dateFormat = SimpleDateFormat(format, Locale.getDefault())

        dateFormat.format(currentDate)

    } catch (e: Exception) {
        ""
    }
}

fun String.isEmail(): Boolean {

    return this.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
}