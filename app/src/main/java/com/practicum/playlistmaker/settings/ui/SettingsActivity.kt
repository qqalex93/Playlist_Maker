package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.app.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            viewModel.shareApp(getString(R.string.share_app))
        }

        binding.writeToSupport.setOnClickListener {
           viewModel.writeToSupport(
               getString(R.string.recipient_address),
               getString(R.string.theme_message),
               getString(R.string.message_content)
               )
        }

            binding.agreement.setOnClickListener {
            viewModel.userAgreement(getString(R.string.link_to_user_agreement))
        }

        viewModel.getIsDefaultThemeDarkLiveData().observe(this) { isDefaultThemeDark ->
            binding.switcher.isChecked = isDefaultThemeDark
        }

        binding.switcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeTheme(isChecked)
        }
    }
}