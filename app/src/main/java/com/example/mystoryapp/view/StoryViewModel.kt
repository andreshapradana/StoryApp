package com.example.mystoryapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStories().asLiveData()
}