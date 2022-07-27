package com.app.example.auth.login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.example.auth.login.data.LoginUser
import com.app.example.auth.login.data.RequestLoginUser
import com.app.example.base.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: AppRepository
): ViewModel(){

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val userMutableLiveData = MutableLiveData<LoginUser>()
    val loggedUser = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    private var job: Job? = null

    fun onLoginClicked() {
        if (email.value != null && password.value != null) {
            val loginUser = LoginUser(email.value!!.trim(), password.value!!)
            userMutableLiveData.value = loginUser
        }
    }

    fun loginUser(){
        job = CoroutineScope(Dispatchers.IO).launch {
            val userResponse = repository.loginUser("", RequestLoginUser(email.value!!, password.value!!))
            withContext(Dispatchers.Main) {
                if (userResponse.isSuccessful) {
                    if (userResponse.body() != null) {
                        saveUserState()
                    } else {
                        onError("Korisnik ne postoji")
                    }
                } else {
                    onError("Korisnik ne postoji")
                }
            }
        }
    }

    private suspend fun saveUserState() {
//        repository.saveSignedUpUserState()
        loggedUser.value = true

    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}