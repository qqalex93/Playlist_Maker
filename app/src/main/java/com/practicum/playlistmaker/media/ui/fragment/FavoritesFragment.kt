package com.practicum.playlistmaker.media.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.presentation.models.FavoriteScreenState
import com.practicum.playlistmaker.media.presentation.models.PlaylistsScreenState
import com.practicum.playlistmaker.media.presentation.view_model.FavoritesFragmentViewModel
import com.practicum.playlistmaker.support.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesFragmentViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenStateObserve().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteScreenState.Empty -> showEmpty()
                is FavoriteScreenState.Content -> showContent()
            }
        }
    }

    private fun showEmpty() {
        val emptyImageId: Int = getEmptyImageIdAccTheme(
            R.drawable.ic_error_search_result_dm,
            R.drawable.ic_error_search_result_nm
        )

        binding.ivErrorImage.setImageResource(emptyImageId)
        binding.tvErrorMessage.text = requireActivity().getString(R.string.empty_favorite_message)
        binding.groupWidgetEmpty.isVisible = true
    }

    private fun showContent() {
        binding.groupWidgetEmpty.isVisible = false
    }

    private fun getEmptyImageIdAccTheme(imageIdLightMode: Int, imageIdDarkMode: Int): Int {
        return when (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> imageIdDarkMode
            Configuration.UI_MODE_NIGHT_NO -> imageIdLightMode
            else -> imageIdDarkMode
        }
    }

    companion object {
        fun newInstance(): FavoritesFragment = FavoritesFragment()
    }
}
