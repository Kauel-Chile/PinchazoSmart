package com.kauel.pinchazoSmart.utils

import androidx.room.TypeConverter
import com.kauel.pinchazoSmart.api.portico.Data
import com.kauel.pinchazoSmart.api.portico.InfoPortico
import com.kauel.pinchazoSmart.api.volumen.InfoVolumen
import com.kauel.pinchazoSmart.api.volumen.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kauel.pinchazoSmart.api.login.DataLogin
import java.util.*

class Converters {

    @TypeConverter
    fun restoreInfoPorticoData(objectToRestore: String?): InfoPortico? {
        return Gson().fromJson(objectToRestore, object : TypeToken<InfoPortico?>() {}.type)
    }

    @TypeConverter
    fun saveInfoPorticoData(objectToSave: InfoPortico?): String? {
        return Gson().toJson(objectToSave)
    }

    @TypeConverter
    fun restoreInfoVolumenData(objectToRestore: String?): InfoVolumen? {
        return Gson().fromJson(objectToRestore, object : TypeToken<InfoVolumen?>() {}.type)
    }

    @TypeConverter
    fun saveInfoVolumenData(objectToSave: InfoVolumen?): String? {
        return Gson().toJson(objectToSave)
    }

    @TypeConverter
    fun restoreInfoVolumenDataSend(objectToRestore: String?): Item? {
        return Gson().fromJson(objectToRestore, object : TypeToken<Item?>() {}.type)
    }

    @TypeConverter
    fun saveInfoVolumenDataSend(objectToSave: Item?): String? {
        return Gson().toJson(objectToSave)
    }

    @TypeConverter
    fun restoreInfoListPortico(objectToRestore: String?): List<Data>? {
        return Gson().fromJson(objectToRestore, object : TypeToken<List<Data>?>() {}.type)
    }

    @TypeConverter
    fun saveInfoListPortico(objectToSave: List<Data>?): String? {
        return Gson().toJson(objectToSave)
    }

    @TypeConverter
    fun restoreSendData(objectToRestore: String?): Date? {
        return Gson().fromJson(objectToRestore, object : TypeToken<Date?>() {}.type)
    }

    @TypeConverter
    fun saveSendData(objectToSave: Date?): String? {
        return Gson().toJson(objectToSave)
    }

    @TypeConverter
    fun restoreResponseDataLogin(objectToRestore: String?): DataLogin? {
        return Gson().fromJson(objectToRestore, object : TypeToken<DataLogin?>() {}.type)
    }

    @TypeConverter
    fun saveResponseDataLogin(objectToSave: DataLogin?): String? {
        return Gson().toJson(objectToSave)
    }
}