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
            Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5–ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a–2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin\\' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track("Sweet Child O\\'Mine","Guns N\\' Roses","5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
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