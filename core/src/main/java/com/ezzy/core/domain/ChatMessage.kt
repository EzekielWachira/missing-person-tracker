package com.ezzy.core.domain

import java.io.Serializable

data class ChatMessage(
    private val message: String? = null,
    private val messageTime: Long? = null,
    private val readStatus: Boolean? = null
): Serializable
