package com.example.mystoryapp.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.story.StoryActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                showLoading(true)
                lifecycleScope.launch {
                    val result = viewModel.login(email, password)
                    showLoading(false)
                    result.fold(
                        onSuccess = { token ->
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Yeah!")
                                setMessage("Anda berhasil login. Sudah tidak sabar untuk berbagi cerita ya?")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, StoryActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        },
                        onFailure = { error ->
                            Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            } else {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}