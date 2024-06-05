package com.example.mystoryapp.data.repository

import com.example.mystoryapp.data.api.ApiService
import com.example.mystoryapp.data.response.ErrorResponse
import com.example.mystoryapp.data.pref.UserModel
import com.example.mystoryapp.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): Result<String> {
        return try {
            val response = apiService.register(name, email, password)
            if (!response.error) {
                Result.success(response.message)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Result.failure(Exception(errorResponse.message))
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = apiService.login(email, password)
            if (!response.error) {
                saveSession(UserModel(email, response.loginResult.token, true))
                Result.success(response.loginResult.token)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            val errorResponse = parseError(e)
            Result.failure(Exception(errorResponse.message))
        }
    }

    private fun parseError(e: HttpException): ErrorResponse {
        val jsonInString = e.response()?.errorBody()?.string()
        return Gson().fromJson(jsonInString, ErrorResponse::class.java)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ) = UserRepository(userPreference, apiService)
}}