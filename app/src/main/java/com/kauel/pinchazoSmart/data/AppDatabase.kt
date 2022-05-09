package com.kauel.pinchazoSmart.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kauel.pinchazoSmart.api.login.ResponseDataLogin
import com.kauel.pinchazoSmart.api.login.ResponseLogin
import com.kauel.pinchazoSmart.api.portico.ListPortico
import com.kauel.pinchazoSmart.api.portico.Portico
import com.kauel.pinchazoSmart.api.portico.PorticoStatus
import com.kauel.pinchazoSmart.api.volumen.ResponseSendDataVolumen
import com.kauel.pinchazoSmart.api.volumen.SendDataVolumen
import com.kauel.pinchazoSmart.api.volumen.Volumen
import com.kauel.pinchazoSmart.utils.Converters

@Database(
    entities = [PorticoStatus::class,
        Volumen::class,
        ResponseSendDataVolumen::class,
        ListPortico::class,
        SendDataVolumen::class,
        ResponseDataLogin::class],
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

    abstract fun loginDao(): LoginDao

}