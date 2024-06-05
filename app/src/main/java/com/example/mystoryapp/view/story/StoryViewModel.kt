package com.example.mystoryapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _liststory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _liststory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStories() {
        _isLoading.value = true
        viewModelScope.launch {
            _liststory.value = storyRepository.getStories()
            _isLoading.value = false
        }
    }
}
