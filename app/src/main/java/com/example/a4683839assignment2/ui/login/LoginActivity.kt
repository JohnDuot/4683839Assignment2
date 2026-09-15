package com.example.a4683839assignment2.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.a4683839assignment2.databinding.ActivityLoginBinding
import com.example.a4683839assignment2.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text?.toString().orEmpty()
            val password = binding.passwordEditText.text?.toString().orEmpty()
            viewModel.login(username, password)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        LoginUiState.Idle -> Unit
                        LoginUiState.Loading -> {
                            binding.loginButton.isEnabled = false
                            binding.loginButton.text = "Signing in..."
                            binding.errorText.text = ""
                        }
                        is LoginUiState.Success -> {
                            binding.loginButton.isEnabled = true
                            binding.loginButton.text = "Login"
                            startActivity(
                                Intent(this@LoginActivity, DashboardActivity::class.java)
                                    .putExtra(DashboardActivity.EXTRA_KEYPASS, state.keypass)
                            )
                            // Prevent the retained Success state from reopening Dashboard
                            // if the user navigates back to this Activity.
                            finish()
                        }
                        is LoginUiState.Error -> {
                            binding.loginButton.isEnabled = true
                            binding.loginButton.text = "Login"
                            binding.errorText.text = state.message
                        }
                    }
                }
            }
        }
    }
}
