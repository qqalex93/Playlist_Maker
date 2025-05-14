package com.practicum.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.ui.adapter.MediaViewPagerAdapter
import com.practicum.playlistmaker.support.BindingFragment
import com.practicum.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : BindingFragment<FragmentMediaBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMediaBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaViewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.mediaTab, binding.mediaViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.favorite_tracks)
                else -> getString(R.string.playlists)
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mediaTab.clearOnTabSelectedListeners()
    }
}




