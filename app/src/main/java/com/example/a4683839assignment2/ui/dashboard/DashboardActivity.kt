package com.example.a4683839assignment2.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4683839assignment2.databinding.ActivityDashboardBinding
import com.example.a4683839assignment2.ui.details.DetailsActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KEYPASS = "keypass"
    }

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keypass = intent.getStringExtra(EXTRA_KEYPASS)
        if (keypass.isNullOrBlank()) {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = "Unable to open dashboard because the login keypass is missing."
            binding.dashboardSubtitle.text = "Topic unavailable"
            return
        }

        binding.dashboardSubtitle.text = "Topic: $keypass"

        val adapter = EntityAdapter { entity ->
            startActivity(
                Intent(this, DetailsActivity::class.java)
                    .putExtra(DetailsActivity.EXTRA_ENTITY_JSON, gson.toJson(entity))
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        DashboardUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                        is DashboardUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.entityCount.text = "${state.data.entityTotal} items"
                            adapter.submitList(state.data.entities)
                        }
                        is DashboardUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.VISIBLE
                            binding.errorText.text = state.message
                        }
                    }
                }
            }
        }

        viewModel.loadDashboard(keypass)
    }
}
