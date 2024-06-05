package com.example.mystoryapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.story.StoryActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity

class LauncherActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, StoryActivity::class.java))
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            finish()
        }
    }
}