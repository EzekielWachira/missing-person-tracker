package com.ezzy.core.domain

import java.io.Serializable

class Contact(
    val contactName: String? = null,
    val contactNumber: String? = null,
    val contactEmail: String? = null
): Serializable