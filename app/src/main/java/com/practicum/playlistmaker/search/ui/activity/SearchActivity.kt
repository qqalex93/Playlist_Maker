package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.presentation.model.SearchTrackInfo
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.search.presentation.model.ErrorTypePresenter
import com.practicum.playlistmaker.search.presentation.model.SearchScreenState
import com.practicum.playlistmaker.search.presentation.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.search.ui.model.ErrorInfo

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initHistoryAdapter()

        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

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
            this, LinearLayoutManager.VERTICAL,
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
                    getString(R.string.nothing_found_message),
                    getErrorImageIdAccordingTheme(
                        R.drawable.ic_error_search_result_dm,
                        R.drawable.ic_error_search_result_nm
                    ),
                    false
                )
            }

            is ErrorTypePresenter.NoConnection -> {
                return ErrorInfo(
                    getString(R.string.bad_connection_message),
                    getErrorImageIdAccordingTheme(
                        R.drawable.ic_error_search_result_dm,
                        R.drawable.ic_error_search_result_nm
                    ),
                    true
                )
            }

            is ErrorTypePresenter.BadRequest, is ErrorTypePresenter.ErrorServer -> {
                return ErrorInfo(
                    getString(R.string.something_wrong_message),
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
        return when (getResources().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
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
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(TRACK_KEY, trackId)
        startActivity(intent)
    }

    private fun clearFocusEditText() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchLine.windowToken, 0)
        binding.searchLine.clearFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.rwTrackList.clearOnScrollListeners()
    }

    private companion object {
        const val STRING_DEF_VALUE = ""
        const val TRACK_KEY = "track"
    }
}