package com.example.mystoryapp.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mystoryapp.data.StoryPagingSource
import com.example.mystoryapp.data.api.ApiConfig
import com.example.mystoryapp.data.api.ApiService
import com.example.mystoryapp.data.response.ListStoryItem
import com.example.mystoryapp.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

    suspend fun getStories(): List<ListStoryItem> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userPreference.getSession().first()
                Log.d(TAG, "getStories: ${user.token}")
                val token = user.token
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.getStories()
                response.listStory.map { storyResponse ->
                    ListStoryItem(
                        id = storyResponse.id,
                        name = storyResponse.name,
                        description = storyResponse.description,
                        photoUrl = storyResponse.photoUrl
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun getStoriesWithPaging(): Flow<PagingData<ListStoryItem>> {
        val token = getTokenBlocking()
        val apiServiceWithToken = ApiConfig.getApiService(token)
        Log.d("StoryRepository", "getStoriesWithPaging: token=$token")
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiServiceWithToken)}
        ).flow
    }
    private fun getTokenBlocking(): String {
        return runBlocking {
            userPreference.getSession().first().token
        }
    }
    suspend fun getStoriesWithLocation(): List<ListStoryItem> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userPreference.getSession().first()
                Log.d(TAG, "getStories: ${user.token}")
                val token = user.token
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.getStoriesWithLocation()
                response.listStory.map { storyResponse ->
                    ListStoryItem(
                        id = storyResponse.id,
                        name = storyResponse.name,
                        description = storyResponse.description,
                        photoUrl = storyResponse.photoUrl,
                        lat = storyResponse.lat,
                        lon = storyResponse.lon
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "getStoriesWithLocation: ${e.message}", e)
                emptyList()
            }
        }
    }

}