package com.practicum.playlistmaker.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.presentation.ui.App
import com.practicum.playlistmaker.R
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        val shareApp = findViewById<TextView>(R.id.share_app)
        val writeToSupport = findViewById<TextView>(R.id.write_to_support)
        val userAgreement = findViewById<TextView>(R.id.agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switcher)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        shareApp.setOnClickListener {
            val message = getString(R.string.link_to_course)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            val chooser = Intent.createChooser(shareIntent, getString(R.string.app_choose))
            startActivity(chooser)

        }

        writeToSupport.setOnClickListener {
            val messageContent = getString(R.string.message_content)
            val themeMessage = getString(R.string.theme_message)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = "mailto:".toUri()
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.recipient_address)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, messageContent)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, themeMessage)
            startActivity(shareIntent)
        }

        userAgreement.setOnClickListener {
            val linkToAgreement = getString(R.string.link_to_user_agreement)
            val shareIntent = Intent(Intent.ACTION_VIEW).apply {
                data = linkToAgreement.toUri()
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
            startActivity(shareIntent)

        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }
        themeSwitcher.isChecked = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
    }
}