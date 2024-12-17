package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.internal.ViewUtils.showKeyboard
import com.practicum.playlistmaker.databinding.ActivityMainBinding

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

        searchLine.setOnFocusChangeListener { v, hasFocus ->
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