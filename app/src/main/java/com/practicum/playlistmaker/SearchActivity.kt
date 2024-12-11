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

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchLineText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchLine.addTextChangedListener(textWatcher)
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

    companion object {

        private const val KEY_SEARCH_LINE_TEXT = "SEARCH_LINE_TEXT"
        private const val KEY_SEARCH_LINE_FOCUS = "SEARCH_LINE_FOCUS"

    }





//        private fun clearButtonVisibility(s: CharSequence?): Int {
//            return if (s.isNullOrEmpty()) {
//                View.GONE
//            } else {
//                View.VISIBLE
//            }
//        }
}