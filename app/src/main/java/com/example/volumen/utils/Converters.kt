package com.example.volumen.utils

import androidx.room.TypeConverter
import com.example.volumen.api.portico.Data
import com.example.volumen.api.portico.InfoPortico
import com.example.volumen.api.volumen.InfoVolumen
import com.example.volumen.api.volumen.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
}