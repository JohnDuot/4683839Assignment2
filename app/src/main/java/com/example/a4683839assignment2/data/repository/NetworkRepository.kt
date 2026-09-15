package com.example.a4683839assignment2.data.repository

import com.example.a4683839assignment2.data.api.ApiService
import com.example.a4683839assignment2.data.model.DashboardResponse
import com.example.a4683839assignment2.data.model.LoginRequest
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val apiService: ApiService
) : AppRepository {
    override suspend fun login(username: String, password: String): String {
        return apiService.login(LoginRequest(username, password)).keypass
    }

    override suspend fun getDashboard(keypass: String): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}
