package com.example.a4683839assignment2.data.api

import com.example.a4683839assignment2.data.model.DashboardResponse
import com.example.a4683839assignment2.data.model.LoginRequest
import com.example.a4683839assignment2.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("footscray/auth")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
