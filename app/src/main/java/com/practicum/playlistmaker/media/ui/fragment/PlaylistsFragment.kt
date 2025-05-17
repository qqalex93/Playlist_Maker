package com.practicum.playlistmaker.media.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.presentation.models.PlaylistsScreenState
import com.practicum.playlistmaker.media.presentation.view_model.PlaylistsFragmentViewModel
import com.practicum.playlistmaker.support.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: BindingFragment<FragmentPlaylistsBinding>() {

    private val viewModel: PlaylistsFragmentViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenStateObserve().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsScreenState.Empty -> showEmpty()
                is PlaylistsScreenState.Content -> showContent()
            }
        }
    }

    private fun showEmpty() {
        val emptyImageId: Int = getEmptyImageIdAccTheme(
            R.drawable.ic_error_search_result_dm,
            R.drawable.ic_error_search_result_nm
        )

        binding.ivErrorImage.setImageResource(emptyImageId)
        binding.tvErrorMessage.text = requireActivity().getString(R.string.empty_playlist_message)
        binding.groupPlWidgetEmpty.isVisible = true
    }

    private fun showContent() {
        binding.groupPlWidgetEmpty.isVisible = false
    }

    private fun getEmptyImageIdAccTheme(imageIdLightMode: Int, imageIdDarkMode: Int): Int {
        return when (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> imageIdDarkMode
            Configuration.UI_MODE_NIGHT_NO -> imageIdLightMode
            else -> imageIdDarkMode
        }
    }

    companion object {
        fun newInstance(): PlaylistsFragment = PlaylistsFragment()
    }
}
