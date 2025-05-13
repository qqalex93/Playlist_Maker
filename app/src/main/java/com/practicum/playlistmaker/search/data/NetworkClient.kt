package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.NetworkResponse

interface NetworkClient {
    fun doRequest(dto: Any) : NetworkResponse
}