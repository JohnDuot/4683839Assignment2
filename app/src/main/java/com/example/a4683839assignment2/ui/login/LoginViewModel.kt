package com.example.a4683839assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a4683839assignment2.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val keypass: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun login(username: String, password: String) {
        val cleanUsername = username.trim()

        if (cleanUsername.isBlank() || password.isBlank()) {
            _state.value = LoginUiState.Error("Please enter both username and password.")
            return
        }

        if (!cleanUsername.all(Char::isDigit)) {
            _state.value = LoginUiState.Error("Student ID must contain numbers only.")
            return
        }

        viewModelScope.launch {
            _state.value = LoginUiState.Loading
            try {
                val keypass = repository.login(cleanUsername, password)
                if (keypass.isBlank()) {
                    _state.value = LoginUiState.Error("Login succeeded but no keypass was returned.")
                } else {
                    _state.value = LoginUiState.Success(keypass)
                }
            } catch (e: HttpException) {
                _state.value = if (e.code() == 401 || e.code() == 403) {
                    LoginUiState.Error("Login failed. Check your student ID and first name.")
                } else {
                    LoginUiState.Error("Server error (${e.code()}). Please try again.")
                }
            } catch (e: IOException) {
                _state.value = LoginUiState.Error("Network error. Check your internet connection and try again.")
            } catch (e: Exception) {
                _state.value = LoginUiState.Error("Login failed. Please try again.")
            }
        }
    }
}
