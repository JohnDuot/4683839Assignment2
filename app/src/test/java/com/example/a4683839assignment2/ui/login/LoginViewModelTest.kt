package com.example.a4683839assignment2.ui.login

import com.example.a4683839assignment2.data.model.DashboardResponse
import com.example.a4683839assignment2.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful login returns keypass`() = runTest {
        val repository = FakeRepository()
        val viewModel = LoginViewModel(repository)

        viewModel.login("4683839", "John")
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(LoginUiState.Success("animals"), viewModel.state.value)
    }

    @Test
    fun `blank fields return validation error`() {
        val viewModel = LoginViewModel(FakeRepository())
        viewModel.login("", "")

        assertTrue(viewModel.state.value is LoginUiState.Error)
    }

    private class FakeRepository : AppRepository {
        override suspend fun login(username: String, password: String): String = "animals"
        override suspend fun getDashboard(keypass: String): DashboardResponse =
            DashboardResponse(emptyList(), 0)
    }
}
