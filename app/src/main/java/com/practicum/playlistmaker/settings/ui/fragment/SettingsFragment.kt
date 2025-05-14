package com.practicum.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.support.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requiredActivity = requireActivity()

        binding.shareApp.setOnClickListener {
            viewModel.shareApp(requiredActivity.getString(R.string.share_app))
        }

        binding.writeToSupport.setOnClickListener {
            viewModel.writeToSupport(
                requiredActivity.getString(R.string.recipient_address),
                requiredActivity.getString(R.string.theme_message),
                requiredActivity.getString(R.string.message_content)
            )
        }

        binding.agreement.setOnClickListener {
            viewModel.userAgreement(requiredActivity.getString(R.string.link_to_user_agreement))
        }

        viewModel.getIsDefaultThemeDarkLiveData()
            .observe(viewLifecycleOwner) { isDefaultThemeDark ->
                binding.switcher.isChecked = isDefaultThemeDark
            }

        binding.switcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeTheme(isChecked)
        }
    }
}