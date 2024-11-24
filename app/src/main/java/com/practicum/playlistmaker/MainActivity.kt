package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Не нажимай кнопку \"Настройки\"", Toast.LENGTH_SHORT).show()
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)
        mediaButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.media_button -> {
                Toast.makeText(this, "Смотри значение кнопки выше", Toast.LENGTH_SHORT).show()
            }
            R.id.settings_button -> {
                Toast.makeText(this, "Ну вот и зачем, спрашивается?", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
