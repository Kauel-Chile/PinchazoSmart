package com.example.volumen.ui.main

import androidx.lifecycle.*
import com.example.volumen.api.volumen.CodeRequest
import com.example.volumen.api.volumen.ResponseSendDataVolumen
import com.example.volumen.api.volumen.SendDataVolumen
import com.example.volumen.api.volumen.Volumen
import com.example.volumen.data.repository.Repository
import com.example.volumen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VolumenViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val volumenMutableLiveData = MutableLiveData<Resource<Volumen>>()
    val volumenLiveData: LiveData<Resource<Volumen>> = volumenMutableLiveData

    fun getVolumen(url: String, code: CodeRequest) =
        viewModelScope.launch {
            repository.getVolumen(url, code).collect {
                volumenMutableLiveData.value = it
            }
        }

    private val sendDataVolumenMutableLiveData = MutableLiveData<Resource<ResponseSendDataVolumen>>()
    val sendDataVolumenLiveData: LiveData<Resource<ResponseSendDataVolumen>> = sendDataVolumenMutableLiveData

    fun sendDataVolumen(url: String, data: SendDataVolumen) =
        viewModelScope.launch {
            repository.sendDataVolumen(url, data).collect {
                sendDataVolumenMutableLiveData.value = it
            }
        }

//    fun insertSendDataVolumen(data: SendDataVolumen) =
//        viewModelScope.launch {
//            repository.insertSendData(data)
//        }
//
//    private val serverTask = sendDataVolumenDao.getAllSendDataVolumen()
//    val listData = serverTask.asLiveData()

}