package com.kauel.pinchazoSmart.ui.portico

import androidx.lifecycle.*
import com.kauel.pinchazoSmart.api.portico.PorticoStatus
import com.kauel.pinchazoSmart.api.portico.ListPortico
import com.kauel.pinchazoSmart.data.repository.Repository
import com.kauel.pinchazoSmart.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PorticoViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){

    private val porticoMutableLiveData = MutableLiveData<Resource<PorticoStatus>>()
    val porticoLiveData: LiveData<Resource<PorticoStatus>> = porticoMutableLiveData

    fun getPorticoStatus(url: String) =
        viewModelScope.launch {
            repository.getPorticoStatus(url).collect {
                porticoMutableLiveData.value = it
            }
        }

    private val listPorticoMutableLiveData = MutableLiveData<Resource<ListPortico>>()
    val listPorticoLiveData: LiveData<Resource<ListPortico>> = listPorticoMutableLiveData

    fun getListPortico(url: String) =
        viewModelScope.launch {
            repository.getListPortico(url).collect {
                listPorticoMutableLiveData.value = it
            }
        }

}