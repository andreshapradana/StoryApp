package com.example.mystoryapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            _stories.value = storyRepository.getStoriesWithLocation()
        }
    }
}