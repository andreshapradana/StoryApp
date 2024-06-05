package com.example.mystoryapp.view.signup

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            repository.register(name, email, password)
        }
    }
}