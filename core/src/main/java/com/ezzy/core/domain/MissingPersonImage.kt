package com.ezzy.core.domain

import java.io.Serializable

class MissingPersonImage(
    val missingPersonId: String? = null,
    val imageUrl: String? = null
): Serializable