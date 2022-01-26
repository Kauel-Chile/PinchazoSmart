package com.example.volumen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.volumen.api.portico.ListPortico
import com.example.volumen.api.portico.PorticoStatus
import com.example.volumen.api.volumen.ResponseSendDataVolumen
import com.example.volumen.api.volumen.SendDataVolumen
import com.example.volumen.api.volumen.Volumen
import com.example.volumen.utils.Converters

@Database(
    entities = [PorticoStatus::class,
        Volumen::class,
        ResponseSendDataVolumen::class,
        ListPortico::class,
        SendDataVolumen::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun porticoDao(): PorticoDao

    abstract fun volumenDao(): VolumenDao

    abstract fun dataVolumenDao(): DataVolumenDao

    abstract fun listPorticoDao(): ListPorticoDao

    abstract fun sendDataDao(): SendDataDao

}