package com.example.mystoryapp.view.signup

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivitySignupBinding
import com.example.mystoryapp.view.ViewModelFactory
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val result = viewModel.register(name, email, password)
                    result.fold(
                        onSuccess = { message ->
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("Yeah!")
                                setMessage("Akun dengan email $email sudah jadi nih. Yuk, login dan belajar coding.")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                        },
                        onFailure = { error ->
                            Toast.makeText(this@SignupActivity, error.message, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            } else {
                Toast.makeText(this, "Nama, email, dan password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}