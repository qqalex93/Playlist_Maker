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
import com.practicum.playlistmaker.creator.Creator

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private val settingsInteractor = Creator.provideSettingsInteractor()
    private val externalActionsInteractor = Creator.provideExternalActionsInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val shareApp = findViewById<TextView>(R.id.share_app)
        shareApp.setOnClickListener {
            externalActionsInteractor.shareApp(getString(R.string.share_app))
        }

        val writeToSupport = findViewById<TextView>(R.id.write_to_support)
        writeToSupport.setOnClickListener {
           externalActionsInteractor.writeToSupport(
               getString(R.string.recipient_address),
               getString(R.string.theme_message),
               getString(R.string.message_content)
               )
        }

        val userAgreement = findViewById<TextView>(R.id.agreement)
        userAgreement.setOnClickListener {
            externalActionsInteractor.userAgreement(getString(R.string.link_to_user_agreement))
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switcher)
        val currentTheme = settingsInteractor.getTheme()
        themeSwitcher.isChecked = currentTheme
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }
    }
}