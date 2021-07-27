package com.ezzy.core.domain

import java.io.Serializable

data class Address(
    val country: String? = null,
    val city: String? = null,
    val state: String? = null,
    val street: String? = null,
): Serializable
