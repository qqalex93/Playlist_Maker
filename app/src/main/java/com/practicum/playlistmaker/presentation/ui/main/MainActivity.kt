package com.practicum.playlistmaker.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity
import com.practicum.playlistmaker.presentation.ui.settings.SettingsActivity
import com.practicum.playlistmaker.presentation.ui.mediaLibrary.MediaActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)


        searchButton.setOnClickListener(this)
        mediaButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)


    }
    override fun onClick(v: View?) {
        val intent = when(v?.id) {
            R.id.search_button -> Intent(this, SearchActivity::class.java)
            R.id.media_button -> Intent(this, MediaActivity::class.java)
            R.id.settings_button -> Intent(this, SettingsActivity::class.java)
            else -> null
        }
        startActivity(intent)

    }
}
