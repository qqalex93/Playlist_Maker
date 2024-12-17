package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        val shareApp = findViewById<TextView>(R.id.share_app)
        val writeToSupport = findViewById<TextView>(R.id.write_to_support)
        val userAgreement = findViewById<TextView>(R.id.agreement)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.switcher)

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
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.recipient_address)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, messageContent)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, themeMessage)
            startActivity(shareIntent)
        }

        userAgreement.setOnClickListener {
            val linkToAgreement = getString(R.string.link_to_user_agreement)
            val shareIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(linkToAgreement)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }
            startActivity(shareIntent)

        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        themeSwitch.isChecked = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
    }
}