package com.app.example.base

import com.app.example.auth.login.data.RequestLoginUser
import com.app.example.auth.registration.data.RequestRegisterUser
import com.app.example.base.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

interface Api {
    companion object {
        const val BASE_URL = "http://androidkurs.itcentar.rs/api/"
    }

    suspend fun registerUser(
        @Header("Authorization") token: String,
        @Body registerUser: RequestRegisterUser
    ): Response<User>

    suspend fun loginUser(
        @Header("Authorization") token: String,
        @Body loginUser: RequestLoginUser
    ): Response<User>

}