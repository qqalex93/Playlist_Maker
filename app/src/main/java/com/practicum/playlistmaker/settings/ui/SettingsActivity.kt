package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

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