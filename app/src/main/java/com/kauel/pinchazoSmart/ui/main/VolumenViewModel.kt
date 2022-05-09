package com.kauel.pinchazoSmart.ui.main

import androidx.lifecycle.*
import com.kauel.pinchazoSmart.api.volumen.*
import com.kauel.pinchazoSmart.data.SendDataDao
import com.kauel.pinchazoSmart.data.repository.Repository
import com.kauel.pinchazoSmart.utils.Resource
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

    private val sendDataMutableLiveData = MutableLiveData<Resource<Boolean>>()
    val sendDataLiveData: LiveData<Resource<Boolean>> = sendDataMutableLiveData

    fun sendData(url: String, data: SendDataVolumen) {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                repository.sendData(data, url).collect {
                    sendDataMutableLiveData.postValue(it)
                }
            }
        }
    }

}