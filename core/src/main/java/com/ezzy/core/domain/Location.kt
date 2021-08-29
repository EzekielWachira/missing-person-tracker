package com.ezzy.core.domain

import java.io.Serializable

data class Location(
    val missingPersonId: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val locationName: String? = null
): Serializable
