package com.example.mystoryapp.view.login

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            repository.login(email, password)
        }
    }
}