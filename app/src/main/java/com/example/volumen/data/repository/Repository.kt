package com.example.volumen.data.repository

import androidx.lifecycle.asLiveData
import androidx.room.withTransaction
import com.example.volumen.api.ApiService
import com.example.volumen.api.portico.Data
import com.example.volumen.api.volumen.CodeRequest
import com.example.volumen.api.volumen.ResponseSendDataVolumen
import com.example.volumen.api.volumen.SendDataVolumen
import com.example.volumen.data.AppDatabase
import com.example.volumen.utils.Resource
import com.example.volumen.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {

    private val porticoDao = appDatabase.porticoDao()
    private val volumenDao = appDatabase.volumenDao()
    private val dataVolumenDao = appDatabase.dataVolumenDao()
    private val listPorticoDao = appDatabase.listPorticoDao()
    //private val sendDataVolumenDao = appDatabase.sendDataVolumenDao()

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

//    suspend fun insertSendData(data: SendDataVolumen) {
//        appDatabase.withTransaction {
//            sendDataVolumenDao.insertSendDataVolumen(data)
//        }
//    }

    fun sendData(
        listData: List<SendDataVolumen>,
        url: String
    ): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading<Boolean>())
            try {
                var response: ResponseSendDataVolumen

                listData.forEach { data ->
                    response = apiService.sendDataVolumen(url, data)
                }

                emit(Resource.Success<Boolean>(true))
            } catch (throwable: Throwable) {
                emit(Resource.Error<Boolean>(throwable))
            }
        }.flowOn(Dispatchers.IO)
    }

}