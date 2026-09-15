package com.example.a4683839assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a4683839assignment2.data.model.DashboardResponse
import com.example.a4683839assignment2.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val data: DashboardResponse) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    fun loadDashboard(keypass: String) {
        if (keypass.isBlank()) {
            _state.value = DashboardUiState.Error("Dashboard keypass is missing.")
            return
        }

        viewModelScope.launch {
            _state.value = DashboardUiState.Loading
            try {
                _state.value = DashboardUiState.Success(repository.getDashboard(keypass))
            } catch (e: HttpException) {
                _state.value = DashboardUiState.Error("Unable to load dashboard (server error ${e.code()}).")
            } catch (e: IOException) {
                _state.value = DashboardUiState.Error("Network error. Check your internet connection and try again.")
            } catch (e: Exception) {
                _state.value = DashboardUiState.Error("Unable to load dashboard data.")
            }
        }
    }
}
