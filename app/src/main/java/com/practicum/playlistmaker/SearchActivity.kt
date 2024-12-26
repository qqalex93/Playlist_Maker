package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private var searchLineText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchToolbar = findViewById<Toolbar>(R.id.search_toolbar)
        val searchLine = findViewById<EditText>(R.id.search_line)
        val clearButton = findViewById<ImageView>(R.id.clear_search_query)


        searchToolbar.setNavigationOnClickListener {
            finish()
        }

        searchLine.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.showSoftInput(searchLine, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        clearButton.setOnClickListener {
            searchLine.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
            searchLine.clearFocus()
        }

        searchLine.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                clearButton.isVisible = !charSequence.isNullOrEmpty()
                searchLineText = charSequence.toString()
            }
        )

        val tracks: List<Track> = listOf(
            Track(getString(R.string.track_name1), getString(R.string.artist_name1), getString(R.string.track_time1), getString(R.string.track_link1)),
            Track(getString(R.string.track_name2), getString(R.string.artist_name2), getString(R.string.track_time2), getString(R.string.track_link2)),
            Track(getString(R.string.track_name3), getString(R.string.artist_name3), getString(R.string.track_time3), getString(R.string.track_link3)),
            Track(getString(R.string.track_name4), getString(R.string.artist_name4), getString(R.string.track_time4), getString(R.string.track_link4)),
            Track(getString(R.string.track_name5), getString(R.string.artist_name5), getString(R.string.track_time5), getString(R.string.track_link5))
        )

        val recyclerView = findViewById<RecyclerView>(R.id.rv_track_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trackAdapter = TrackAdapter(tracks)
        recyclerView.adapter = trackAdapter
    }





    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_LINE_TEXT, searchLineText)
        val searchLine = findViewById<EditText>(R.id.search_line)
        outState.putBoolean(KEY_SEARCH_LINE_FOCUS, searchLine.isFocused)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchLineText = savedInstanceState.getString(KEY_SEARCH_LINE_TEXT, "")
        val searchLine = findViewById<EditText>(R.id.search_line)
        searchLine.setText(searchLineText)
        if (savedInstanceState.getBoolean(KEY_SEARCH_LINE_FOCUS, false)) {
            searchLine.requestFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.showSoftInput(searchLine, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private companion object {

        const val KEY_SEARCH_LINE_TEXT = "SEARCH_LINE_TEXT"
        const val KEY_SEARCH_LINE_FOCUS = "SEARCH_LINE_FOCUS"

    }
}