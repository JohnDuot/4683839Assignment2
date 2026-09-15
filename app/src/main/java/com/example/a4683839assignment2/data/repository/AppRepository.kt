package com.example.a4683839assignment2.data.repository

import com.example.a4683839assignment2.data.model.DashboardResponse

interface AppRepository {
    suspend fun login(username: String, password: String): String
    suspend fun getDashboard(keypass: String): DashboardResponse
}
