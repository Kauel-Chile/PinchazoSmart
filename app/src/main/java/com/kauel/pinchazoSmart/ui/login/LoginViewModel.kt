package com.kauel.pinchazoSmart.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kauel.pinchazoSmart.api.login.Login
import com.kauel.pinchazoSmart.api.login.ResponseDataLogin
import com.kauel.pinchazoSmart.data.repository.Repository
import com.kauel.pinchazoSmart.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val loginMutableLiveData = MutableLiveData<Resource<ResponseDataLogin>>()
    val loginLiveData: LiveData<Resource<ResponseDataLogin>> = loginMutableLiveData

    fun login(url: String, user: Login) =
        viewModelScope.launch {
            repository.getLogin(url, user).collect {
                loginMutableLiveData.value = it
            }
        }

}