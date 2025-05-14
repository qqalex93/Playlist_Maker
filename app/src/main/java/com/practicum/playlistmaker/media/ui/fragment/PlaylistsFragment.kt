package com.practicum.playlistmaker.media.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.presentation.models.PlaylistScreenState
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistFragmentViewModel
import com.practicum.playlistmaker.support.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: BindingFragment<FragmentPlaylistBinding>() {

    private val viewModel: PlaylistFragmentViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenStateObserve().observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }
    }

    private fun updateUI(state: PlaylistScreenState) {
        binding.groupEmpty.isVisible = state is PlaylistScreenState.Empty

        if (state is PlaylistScreenState.Empty) {
            binding.ivErrorImage.setImageResource(emptyImageId)
            binding.tvErrorMessage.text = getString(R.string.empty_playlist_message)
        }
    }

    private val emptyImageId: Int
        get() = if (isDarkMode()) R.drawable.ic_error_search_result_nm
        else R.drawable.ic_error_search_result_dm

    private fun isDarkMode(): Boolean {
        return (requireActivity().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }
}
