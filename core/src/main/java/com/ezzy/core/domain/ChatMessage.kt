package com.ezzy.core.domain

import java.io.Serializable

/**
 * chat message object class
 * */
data class ChatMessage(
    val message: String? = null,
    val messageTime: Long? = null,
    val readStatus: Boolean? = null
): Serializable
