package com.app.example.base

import android.content.SharedPreferences
import com.app.example.auth.login.data.RequestLoginUser
import com.app.example.auth.registration.data.RequestRegisterUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private val api: Api, private val sharedPreferences: SharedPreferences) {

    suspend fun loginUser(token: String, loginUser: RequestLoginUser) = api.loginUser(token, loginUser)
    suspend fun registerUser(token: String, registerUser: RequestRegisterUser) = api.registerUser(token, registerUser)

}