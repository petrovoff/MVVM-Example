package com.app.example.auth.registration.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.example.auth.registration.data.RegisterUser
import com.app.example.auth.registration.data.RequestRegisterUser
import com.app.example.base.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val repository: AppRepository
) : ViewModel() {


    val userRegisterMutableLiveData = MutableLiveData<RegisterUser>()

    val registeredUser = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    private var job: Job? = null

    fun onRegisteredClicked(email: String, password: String, confirmPassword: String) {
        val registerUser = RegisterUser("", email.trim(), password.trim(), confirmPassword.trim())
        userRegisterMutableLiveData.value = registerUser
    }

    fun registerUser() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.registerUser(
                "",
                RequestRegisterUser(
                    userRegisterMutableLiveData.value!!.email,
                    userRegisterMutableLiveData.value!!.password)
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        saveUserState()
                    } else {
                        onError("Neuspesna registracija")
                    }
                } else {
                    onError("Neuspesna registracija")
                }
            }
        }
    }

    private suspend fun saveUserState() {
//        repository.saveSignedUpUserState()
        registeredUser.value = true
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}