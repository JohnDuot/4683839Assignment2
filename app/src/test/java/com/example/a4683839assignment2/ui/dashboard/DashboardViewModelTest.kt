package com.example.a4683839assignment2.ui.dashboard

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
class DashboardViewModelTest {
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
    fun `load dashboard returns repository data`() = runTest {
        val expected = DashboardResponse(emptyList(), 7)
        val viewModel = DashboardViewModel(FakeRepository(expected))

        viewModel.loadDashboard("animals")
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(DashboardUiState.Success(expected), viewModel.state.value)
    }

    @Test
    fun `blank keypass returns error`() {
        val viewModel = DashboardViewModel(FakeRepository(DashboardResponse(emptyList(), 0)))

        viewModel.loadDashboard("")

        assertTrue(viewModel.state.value is DashboardUiState.Error)
    }

    private class FakeRepository(
        private val dashboard: DashboardResponse
    ) : AppRepository {
        override suspend fun login(username: String, password: String): String = "animals"
        override suspend fun getDashboard(keypass: String): DashboardResponse = dashboard
    }
}
