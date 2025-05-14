package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.presentation.model.SearchTrackInfo
import com.practicum.playlistmaker.search.presentation.model.ErrorTypePresenter
import com.practicum.playlistmaker.search.presentation.model.SearchScreenState
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.search.ui.model.ErrorInfo
import com.practicum.playlistmaker.support.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHistoryAdapter()

        binding.searchLine.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onSearchLineFocusChanged(hasFocus)
        }

        binding.clearSearchQuery.setOnClickListener {
            viewModel.clearSearchRequest()
        }

        binding.searchLine.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                viewModel.onSearchLineTextChanged(charSequence.toString())
            },
            afterTextChanged = { }
        )

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.rwTrackList.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL,
            false
        )

        trackAdapter = TrackAdapter { onTrackClicked(it.trackId) }
        binding.rwTrackList.adapter = trackAdapter
        binding.rwTrackList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (binding.searchLine.hasFocus())
                    clearFocusEditText()
            }
        })

        binding.errorSearchButton.setOnClickListener {
            viewModel.searchTrack()
        }

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is SearchScreenState.Default -> {
                    showDefaultState()
                }

                is SearchScreenState.EnteringRequest -> {
                    showEnteringRequest()
                }

                is SearchScreenState.Loading -> {
                    showLoading()
                }

                is SearchScreenState.Content -> {
                    showContent(screenState.tracks)
                }

                is SearchScreenState.Error -> {
                    showError(screenState.errorType)
                }

                is SearchScreenState.History -> {
                    showHistory(screenState.tracks)
                }

                is SearchScreenState.OnTrackClickedEvent -> {
                    openPlayer(screenState.trackId)
                }
            }
        }
    }

    private fun initHistoryAdapter() {
        historyAdapter = TrackAdapter { onTrackClicked(it.trackId) }
        binding.searchHistoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchHistoryList.adapter = historyAdapter
    }

    private fun showDefaultState() {
        clearFocusEditText()
        binding.searchLine.setText(STRING_DEF_VALUE)
        trackAdapter.clearTracks()

        binding.progressBar.visibility = View.GONE
        binding.clearSearchQuery.visibility = View.GONE
        binding.rwTrackList.visibility = View.GONE
        binding.wgErrorSearch.visibility = View.GONE
        binding.rwQueryHistoryList.visibility = View.GONE
    }

    private fun showHistory(tracks: List<SearchTrackInfo>) {
        historyAdapter.updateTracks(tracks)
        trackAdapter.clearTracks()

        binding.progressBar.visibility = View.GONE
        binding.clearSearchQuery.visibility = View.GONE
        binding.rwTrackList.visibility = View.GONE
        binding.wgErrorSearch.visibility = View.GONE
        binding.rwQueryHistoryList.visibility = View.VISIBLE
    }

    private fun showEnteringRequest() {
        binding.progressBar.visibility = View.GONE
        binding.clearSearchQuery.visibility = View.VISIBLE
        binding.rwTrackList.visibility = View.GONE
        binding.wgErrorSearch.visibility = View.GONE
        binding.rwQueryHistoryList.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.clearSearchQuery.visibility = View.VISIBLE
        binding.rwTrackList.visibility = View.GONE
        binding.wgErrorSearch.visibility = View.GONE
        binding.rwQueryHistoryList.visibility = View.GONE
    }

    private fun showContent(tracks: List<SearchTrackInfo>) {
        trackAdapter.updateTracks(tracks)
        binding.progressBar.visibility = View.GONE
        binding.clearSearchQuery.visibility = View.VISIBLE
        binding.rwTrackList.scrollToPosition(0)
        binding.rwTrackList.visibility = View.VISIBLE
        binding.wgErrorSearch.visibility = View.GONE
        binding.rwQueryHistoryList.visibility = View.GONE
    }

    private fun showError(errorType: ErrorTypePresenter) {
        val errorInfo = getErrorInfo(errorType)
        binding.twErrorSearchMessage.text = errorInfo.errorMessage
        binding.iwErrorSearchResult.setImageResource(errorInfo.errorImage)
        binding.errorSearchButton.isVisible = errorInfo.isNeedUpdateButton

        binding.progressBar.visibility = View.GONE
        binding.clearSearchQuery.visibility = View.VISIBLE
        binding.rwTrackList.visibility = View.GONE
        binding.wgErrorSearch.visibility = View.VISIBLE
        binding.rwQueryHistoryList.visibility = View.GONE
    }

    private fun getErrorInfo(errorType: ErrorTypePresenter): ErrorInfo {
        when (errorType) {
            is ErrorTypePresenter.EmptyResult -> {
                return ErrorInfo(
                    requireActivity().getString(R.string.nothing_found_message),
                    getErrorImageIdAccordingTheme(
                        R.drawable.ic_error_search_result_dm,
                        R.drawable.ic_error_search_result_nm
                    ),
                    false
                )
            }

            is ErrorTypePresenter.NoConnection -> {
                return ErrorInfo(
                    requireActivity().getString(R.string.bad_connection_message),
                    getErrorImageIdAccordingTheme(
                        R.drawable.ic_error_search_result_dm,
                        R.drawable.ic_error_search_result_nm
                    ),
                    true
                )
            }

            is ErrorTypePresenter.BadRequest, is ErrorTypePresenter.ErrorServer -> {
                return ErrorInfo(
                    requireActivity().getString(R.string.something_wrong_message),
                    getErrorImageIdAccordingTheme(
                        R.drawable.ic_error_search_result_dm,
                        R.drawable.ic_error_search_result_nm
                    ),
                    false
                )
            }
        }
    }

    private fun getErrorImageIdAccordingTheme(imageIdLightMode: Int, imageIdDarkMode: Int): Int {
        return when (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> imageIdDarkMode

            Configuration.UI_MODE_NIGHT_NO -> imageIdLightMode

            else -> imageIdDarkMode
        }
    }

    private fun onTrackClicked(trackId: Int) {
        viewModel.onTrackClick(trackId)
    }

    private fun openPlayer(trackId: Int) {
        clearFocusEditText()
        viewModel.saveContentStateBeforeOpenPlayer()
        val action = SearchFragmentDirections.actionSearchFragmentToAudioPlayerActivity(trackId)
        findNavController().navigate(action)
    }

    private fun clearFocusEditText() {
        if (binding.searchLine.hasFocus()) {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchLine.windowToken, 0)
            binding.searchLine.clearFocus()
        }
    }

    override fun onDestroyView() {
        binding.rwTrackList.clearOnScrollListeners()
        super.onDestroyView()
    }

    private companion object {
        const val STRING_DEF_VALUE = ""
    }
}