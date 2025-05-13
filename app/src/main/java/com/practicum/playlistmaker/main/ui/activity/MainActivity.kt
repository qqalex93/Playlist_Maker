package com.practicum.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity
import com.practicum.playlistmaker.media.ui.activity.MediaActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            binding.searchButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SearchActivity::class.java
                    )
                )
            }
            binding.mediaButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MediaActivity::class.java
                    )
                )
            }
            binding.settingsButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SettingsActivity::class.java
                    )
                )
            }
        }
    }
}
