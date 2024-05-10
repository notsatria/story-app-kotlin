package com.notsatria.storyapp.ui.auth

import android.content.Intent
import android.graphics.Color
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
import com.notsatria.storyapp.databinding.ActivityRegisterBinding
import com.notsatria.storyapp.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViewModel()

        with(binding) {
            tvGoToLogin.setOnClickListener {
                navigateToActivity(LoginActivity())
            }

            btnRegister.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                Log.d(TAG, "name: $name, email: $email, password: $password")
                Log.d(TAG, "Daftar clicked")

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    showSnackbar("Field tidak boleh kosong")
                    return@setOnClickListener
                }

                registerViewModel.register(name, email, password)
                    .observe(this@RegisterActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    navigateToActivity(LoginActivity())
                                    showSnackbar("Register berhasil")
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
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
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
        startActivity(Intent(this@RegisterActivity, activity::class.java))
        finish()
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}