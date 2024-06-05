package com.example.mystoryapp.view.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityStoryBinding
import com.example.mystoryapp.view.maps.MapsActivity
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.main.MainViewModel
import com.example.mystoryapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeStories()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
                true
            }
            R.id.action_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        storyAdapter.refresh()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Konfirmasi Logout")
            setMessage("Apakah anda ingin logout dari aplikasi?")
            setPositiveButton("Ya") { dialog, which ->
                mainViewModel.logout()
                val intent = Intent(this@StoryActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryPagingAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@StoryActivity)
            adapter = storyAdapter
        }
    }

    private fun observeStories() {
        storyViewModel.listStory.observe(this) { pagingData ->
            lifecycleScope.launch {
                storyAdapter.submitData(pagingData)
            }
        }
    }
}

