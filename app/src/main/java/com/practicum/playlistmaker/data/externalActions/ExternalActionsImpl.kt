package com.practicum.playlistmaker.data.externalActions

import android.app.Application
import android.content.Intent
import com.practicum.playlistmaker.domain.api.repository.ExternalActions
import androidx.core.net.toUri

class ExternalActionsImpl(private val application: Application) : ExternalActions {
    override fun shareApp(link: String) {
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)
    }

    override fun writeToSupport(targetMail: String, themeMessage: String, messageContent: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = "mailto:".toUri()
        intent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(targetMail)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, themeMessage)
        intent.putExtra(Intent.EXTRA_TEXT, messageContent)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun userAgreement(url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            url.toUri()
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}