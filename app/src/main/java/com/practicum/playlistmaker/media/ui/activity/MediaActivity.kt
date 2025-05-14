package com.practicum.playlistmaker.media.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.media.ui.adapter.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private val tabLayoutMediator by lazy {
        TabLayoutMediator(binding.mediaTab, binding.mediaViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorite_tracks)
                1 -> getString(R.string.playlists)
                else -> ""
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
    }

    private fun setupToolbar() {
        binding.mediaToolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupViewPager() {
        binding.mediaViewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}



