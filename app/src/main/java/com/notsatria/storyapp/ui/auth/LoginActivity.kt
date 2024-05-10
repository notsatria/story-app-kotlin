package com.notsatria.storyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.notsatria.storyapp.R
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.databinding.ActivityLoginBinding
import com.notsatria.storyapp.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViewModel()

        with(binding) {
            tvGoToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    showSnackbar(resources.getString(R.string.field_couldnt_be_empty))
                    return@setOnClickListener
                }

                loginViewModel.login(email, password).observe(this@LoginActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                Log.d(TAG, "User data: ${result.data}")
                                showLoading(false)
                                showSnackbar("Login berhasil")
                            }

                            is Result.Error -> {
                                showLoading(false)
                                showSnackbar(result.error)
                            }

                            else -> showLoading(false)
                        }

                    }
                }
            }
        }
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(context = this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbar(
        message: String,
    ) {
        Snackbar.make(binding.main, message, Snackbar.ANIMATION_MODE_SLIDE).show()
    }

    private fun navigateToActivity(activity: AppCompatActivity) {
        startActivity(Intent(this@LoginActivity, activity::class.java))
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}