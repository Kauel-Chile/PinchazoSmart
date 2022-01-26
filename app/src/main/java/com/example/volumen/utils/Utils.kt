package com.example.volumen.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor

/**
 * Valida rut de la forma XXXXXXXX-X
 */
fun validaRut(rut: String): Boolean? {
    val pattern: Pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$")
    val matcher: Matcher = pattern.matcher(rut)
    if (matcher.matches() === false) return false
    val stringRut = rut.split("-".toRegex()).toTypedArray()
    return stringRut[1].lowercase(Locale.getDefault()) == dv(stringRut[0])
}

/**
 * Valida el dígito verificador
 */
private fun dv(rut: String): String? {
    var M = 0
    var S = 1
    var T = rut.toInt()
    while (T != 0) {
        S = (S + T % 10 * (9 - M++ % 6)) % 11
        T = floor(10.let { T /= it; T }.toDouble()).toInt()
    }
    return if (S > 0) (S - 1).toString() else "k"
}

/**
 * Famatear String a RUT chileno con '. -'
 * */
fun formatRut(rut: String): String? {
    var rut = rut
    var cont = 0
    var format: String
    rut = rut.replace(".", "")
    rut = rut.replace("-", "")
    format = "-" + rut.substring(rut.length - 1)
    for (i in rut.length - 2 downTo 0) {
        format = rut.substring(i, i + 1) + format
        cont++
        if (cont == 3 && i != 0) {
            format = ".$format"
            cont = 0
        }
    }
    return format
}

/**
 * Transformar de String to Double
 * */
fun stringToDouble(value: String): Double {
    return value.toDouble()
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
    }
    return false
}