package com.kauel.pinchazoSmart.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.kauel.pinchazoSmart.api.ApiService
import com.kauel.pinchazoSmart.api.login.Login
import com.kauel.pinchazoSmart.api.volumen.CodeRequest
import com.kauel.pinchazoSmart.api.volumen.ResponseSendData
import com.kauel.pinchazoSmart.api.volumen.ResponseSendDataVolumen
import com.kauel.pinchazoSmart.api.volumen.SendDataVolumen
import com.kauel.pinchazoSmart.data.AppDatabase
import com.kauel.pinchazoSmart.utils.Resource
import com.kauel.pinchazoSmart.utils.isOnline
import com.kauel.pinchazoSmart.utils.networkBoundResource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    @ApplicationContext val appContext: Context,
) {

    private val porticoDao = appDatabase.porticoDao()
    private val volumenDao = appDatabase.volumenDao()
    private val dataVolumenDao = appDatabase.dataVolumenDao()
    private val listPorticoDao = appDatabase.listPorticoDao()
    private val sendDataDao = appDatabase.sendDataDao()
    private val loginDao = appDatabase.loginDao()

    fun getPorticoStatus(url: String) = networkBoundResource(
        databaseQuery = {
            porticoDao.getAllPortico()
        },
        networkCall = {
            apiService.getPorticoStatus(url)
        },
        saveCallResult = {
            appDatabase.withTransaction {
                porticoDao.deleteAllPortico()
                porticoDao.insertPortico(it)
            }
        }
    )

    fun getVolumen(url: String, code: CodeRequest) = networkBoundResource(
        databaseQuery = {
            volumenDao.getAllVolumen()
        },
        networkCall = {
            apiService.getVolumen(url, code)
        },
        saveCallResult = {
            appDatabase.withTransaction {
                volumenDao.deleteAllVolumen()
                volumenDao.insertVolumen(it)
            }
        }
    )

    fun sendDataVolumen(url: String, data: SendDataVolumen) = networkBoundResource(
        databaseQuery = {
            dataVolumenDao.getAllDataVolumen()
        },
        networkCall = {
            apiService.sendDataVolumen(url, data)
        },
        saveCallResult = {
            appDatabase.withTransaction {
                dataVolumenDao.deleteAllDataVolumen()
                dataVolumenDao.insertDataVolumen(it)
            }
        }
    )

    fun getListPortico(url: String) = networkBoundResource(
        databaseQuery = {
            listPorticoDao.getAllListPortico()
        },
        networkCall = {
            apiService.getListPortico(url)
        },
        saveCallResult = {
            appDatabase.withTransaction {
                listPorticoDao.deleteAllListPortico()
                listPorticoDao.insertListPortico(it)
            }
        }
    )

    private suspend fun insertSendData(data: SendDataVolumen) {
        appDatabase.withTransaction {
            sendDataDao.insertSendData(data)
        }
    }

    private suspend fun deleteSendData(id: Int) {
        appDatabase.withTransaction {
            sendDataDao.deleteSendData(id)
        }
    }

    suspend fun deleteAllSendData() {
        appDatabase.withTransaction {
            sendDataDao.deleteAllSendData()
        }
    }

    fun sendData(
        data: SendDataVolumen,
        url: String
    ): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading<Boolean>())
            try {
                var status: Boolean = false
                var response: ResponseSendDataVolumen

                insertSendData(data)

                var listData = sendDataDao.getAllSendData()

                if (isOnline(appContext)) {

                    listData.forEach { data ->
                        //deleteSendData(data.id)
                        response = apiService.sendDataVolumen(url, data)
                        if (response.result) {
                            deleteSendData(data.id)
                        }
                    }

                    listData = sendDataDao.getAllSendData()

                    if (listData.isEmpty()) status = true

                }

                emit(Resource.Success<Boolean>(status))
            } catch (throwable: Throwable) {
                emit(Resource.Error<Boolean>(throwable))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getLogin(url: String, user: Login) = networkBoundResource(
        databaseQuery = {
            loginDao.getAllResponseLogin()
        },
        networkCall = {
            apiService.getLogin(url, user)
        },
        saveCallResult = {
            appDatabase.withTransaction {
                loginDao.deleteAllDataResponseLogin()
                loginDao.insertDataResponseLogin(it)
            }
        }
    )

}