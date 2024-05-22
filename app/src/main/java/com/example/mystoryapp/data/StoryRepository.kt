package com.example.mystoryapp.data

import android.content.ContentValues.TAG
import android.util.Log
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.data.api.ApiService
import com.example.mystoryapp.data.api.StoryResponse
import com.example.mystoryapp.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }

    fun getStories(): Flow<Result<StoryResponse>> = flow {
        try {
            val user = userPreference.getSession().first()
            Log.d(TAG, "getStories: ${user.token}")
            val token = user.token
            if (token.isNotBlank()) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.getStories()
                emit(Result.success(response))
            } else {
                emit(Result.failure(Exception("Token is blank")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}