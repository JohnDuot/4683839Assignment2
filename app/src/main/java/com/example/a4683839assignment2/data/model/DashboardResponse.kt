package com.example.a4683839assignment2.data.model

import com.google.gson.JsonElement

data class DashboardResponse(
    val entities: List<Map<String, JsonElement>>,
    val entityTotal: Int
)
