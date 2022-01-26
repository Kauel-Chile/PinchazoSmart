package com.example.volumen.ui.main

import androidx.lifecycle.*
import com.example.volumen.api.volumen.*
import com.example.volumen.data.SendDataDao
import com.example.volumen.data.repository.Repository
import com.example.volumen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VolumenViewModel @Inject constructor(
    private val repository: Repository,
    private val sendData: SendDataDao,
) : ViewModel() {

    private val volumenMutableLiveData = MutableLiveData<Resource<Volumen>>()
    val volumenLiveData: LiveData<Resource<Volumen>> = volumenMutableLiveData

    fun getVolumen(url: String, code: CodeRequest) =
        viewModelScope.launch {
            repository.getVolumen(url, code).collect {
                volumenMutableLiveData.value = it
            }
        }

    private val sendDataVolumenMutableLiveData =
        MutableLiveData<Resource<ResponseSendDataVolumen>>()
    val sendDataVolumenLiveData: LiveData<Resource<ResponseSendDataVolumen>> =
        sendDataVolumenMutableLiveData

    fun sendDataVolumen(url: String, data: SendDataVolumen) =
        viewModelScope.launch {
            repository.sendDataVolumen(url, data).collect {
                sendDataVolumenMutableLiveData.value = it
            }
        }

//    private val sendDataMutableLiveData = MutableLiveData<Resource<List<ResponseSendData>>>()
//    val sendDataLiveData: LiveData<Resource<List<ResponseSendData>>> = sendDataMutableLiveData

    //    fun sendData(url: String, listFile: List<SendDataVolumen>) {
//        viewModelScope.launch(Dispatchers.Main) {
//            withContext(Dispatchers.IO) {
//                repository.sendData(listFile, url).collect {
//                    sendDataMutableLiveData.postValue(it)
//                }
//            }
//        }
//    }

    private val sendDataMutableLiveData = MutableLiveData<Resource<Boolean>>()
    val sendDataLiveData: LiveData<Resource<Boolean>> = sendDataMutableLiveData

    fun sendData(url: String, data: SendDataVolumen) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                repository.sendData2(data, url).collect {
                    sendDataMutableLiveData.postValue(it)
                }
            }
        }
    }

    fun insertSendDataVolumen(data: SendDataVolumen) =
        viewModelScope.launch {
            repository.insertSendData(data)
        }

//    private val sendDataTask = sendData.getAllSendData()
//    val listSendData = sendDataTask.asLiveData()

    fun deleteData(id: Int) =
        viewModelScope.launch {
            repository.deleteSendData(id)
        }

    fun deleteAllData() =
        viewModelScope.launch {
            repository.deleteAllSendData()
        }

}