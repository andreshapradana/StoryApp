package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.UserRepository
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.data.pref.UserPreference
import com.example.mystoryapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}