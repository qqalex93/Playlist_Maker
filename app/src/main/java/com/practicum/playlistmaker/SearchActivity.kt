package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App.Companion.TRACK_KEY
import com.practicum.playlistmaker.RetrofitClient.trackApi
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var searchLineText: String = ""
    private var isResponseVisible: Boolean = false

    private var isClickAllowed: Boolean = true
    private var handler = Handler(Looper.getMainLooper())

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val tracks: MutableList<Track> = mutableListOf()
    private val searchRunnable = Runnable { trackSearch() }

    private lateinit var searchLine: EditText
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var wgErrorSearch: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var updateErrorButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchToolbar = findViewById<Toolbar>(R.id.search_toolbar)
        val clearButton = findViewById<ImageView>(R.id.clear_search_query)
        searchLine = findViewById(R.id.search_line)
        trackRecyclerView = findViewById(R.id.rw_track_list)
        wgErrorSearch = findViewById(R.id.wg_error_search)
        errorImage = findViewById(R.id.iw_error_search_result)
        errorMessage = findViewById(R.id.tw_error_search_message)
        updateErrorButton = findViewById(R.id.error_search_button)
        progressBar = findViewById(R.id.progressBar)

        searchHistory = SearchHistory((applicationContext as App).sharedPreferences)
        val rwHistory = findViewById<RecyclerView>(R.id.search_history_list)
        val wgHistory = findViewById<LinearLayout>(R.id.rw_query_history_list)
        val clearHistoryBtn = findViewById<Button>(R.id.clear_history_btn)

        searchToolbar.setNavigationOnClickListener {
            finish()
        }

        searchLine.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.showSoftInput(searchLine, InputMethodManager.SHOW_IMPLICIT)
            }
            wgHistory.visibility =
                if (hasFocus && searchLine.text.isEmpty() && historyAdapter.tracks.size != 0)
                    View.VISIBLE
                else View.GONE
        }

        clearButton.setOnClickListener {
            searchLine.setText("")
            clearFocusEditText()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            showErrorMessage("", false)
            isResponseVisible = false
        }

        searchLine.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                if (!charSequence.isNullOrEmpty()) {
                    searchDebounce()
                }
                clearButton.isVisible = !charSequence.isNullOrEmpty()
                searchLineText = charSequence.toString()
                wgHistory.visibility =
                    if (searchLine.hasFocus() && charSequence?.isEmpty() == true &&
                        historyAdapter.tracks.size != 0
                    ) View.VISIBLE else View.GONE
                wgErrorSearch.visibility = View.GONE
            },
            afterTextChanged = {
                isResponseVisible = false
            }
        )

        clearHistoryBtn.setOnClickListener {
            searchHistory.clearSearchHistory()
            historyAdapter.tracks.clear()
            historyAdapter.notifyDataSetChanged()
            wgHistory.visibility = View.GONE
            clearFocusEditText()
        }

        historyAdapter = TrackAdapter {
            openPlayer(it)
        }
        historyAdapter.tracks = searchHistory.getTrackFromHistory().toMutableList()
        rwHistory.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )
        rwHistory.adapter = historyAdapter

        trackRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )

        trackAdapter = TrackAdapter {
            openPlayer(it)
        }
        trackAdapter.tracks = tracks
        trackRecyclerView.adapter = trackAdapter

        updateErrorButton.setOnClickListener {
            trackSearch()
            isResponseVisible = true
        }

        trackRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                clearFocusEditText()
            }
        })
    }

    private fun showErrorMessage(message: String, isConnectionError: Boolean) {
        if (message.isNotEmpty()) {
            if (isConnectionError) {
                showErrorImage(R.drawable.ic_bad_connection_dm, R.drawable.ic_bad_connection_nm)
                errorMessage.text = getString(R.string.bad_connection_message)
                errorMessage.visibility = View.VISIBLE
                wgErrorSearch.visibility = View.VISIBLE
                updateErrorButton.visibility = View.VISIBLE
            } else {
                showErrorImage(
                    R.drawable.ic_error_search_result_dm,
                    R.drawable.ic_error_search_result_nm
                )
                errorMessage.text = getString(R.string.nothing_found_message)
                errorMessage.visibility = View.VISIBLE
                wgErrorSearch.visibility = View.VISIBLE
                updateErrorButton.visibility = View.GONE
            }
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
        } else {
            errorImage.visibility = View.GONE
            errorMessage.visibility = View.GONE
            updateErrorButton.visibility = View.GONE
        }
    }

    private fun showErrorImage(imageDayMode: Int, imageNightMode: Int) {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> errorImage.setImageResource(imageNightMode)
            Configuration.UI_MODE_NIGHT_NO -> errorImage.setImageResource(imageDayMode)
        }
        errorImage.visibility = View.VISIBLE
    }

    private fun trackSearch() {
        trackRecyclerView.visibility = View.GONE
        wgErrorSearch.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        trackApi.searchTracks(searchLineText.trim()).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                        trackRecyclerView.visibility = View.VISIBLE
                        showErrorMessage("", false)
                    } else {
                        showErrorMessage(getString(R.string.nothing_found_message), false)
                    }
                } else {
                    showErrorMessage(getString(R.string.nothing_found_message), false)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                showErrorMessage(getString(R.string.bad_connection_message), true)
            }
        })
    }

    private fun openPlayer(track: Track) {
        if (onTrackClickDebounce()) {
            addTrackToHistory(track)
            val intent: Intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            startActivity(intent)
        }
    }

    private fun addTrackToHistory(track: Track) {
        searchHistory.saveTrack(track)
        historyAdapter.tracks = searchHistory.getTrackFromHistory().toMutableList()
        historyAdapter.notifyDataSetChanged()
        clearFocusEditText()
    }

    private fun clearFocusEditText() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
        searchLine.clearFocus()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun onTrackClickDebounce(): Boolean {
        val current: Boolean = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
        trackRecyclerView.clearOnScrollListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_LINE_TEXT, searchLineText)
        outState.putBoolean(KEY_SEARCH_LINE_FOCUS, searchLine.isFocused)
        outState.putBoolean(IS_RESPONSE_VISIBLE, isResponseVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchLineText = savedInstanceState.getString(KEY_SEARCH_LINE_TEXT, "")
        val searchLine = findViewById<EditText>(R.id.search_line)
        searchLine.setText(searchLineText)
        if (searchLineText.isNotEmpty())
            trackSearch()
        if (savedInstanceState.getBoolean(KEY_SEARCH_LINE_FOCUS, false)) {
            searchLine.requestFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.showSoftInput(searchLine, InputMethodManager.SHOW_IMPLICIT)
        }
        isResponseVisible = savedInstanceState.getBoolean(IS_RESPONSE_VISIBLE, false)
        if (isResponseVisible)
            trackSearch()
    }

    private companion object {

        const val KEY_SEARCH_LINE_TEXT = "SEARCH_LINE_TEXT"
        const val KEY_SEARCH_LINE_FOCUS = "SEARCH_LINE_FOCUS"
        const val IS_RESPONSE_VISIBLE = "IS_RESPONSE_VISIBLE"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}