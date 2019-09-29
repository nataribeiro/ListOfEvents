package br.com.natanaelribeiro.basefeature.utils

import android.content.Context

object SystemUtil {

    fun getPixelFromDp(context: Context, dpValue: Int): Int {
        var density = context.resources.displayMetrics.density
        return (dpValue * density).toInt()
    }
}