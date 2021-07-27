package com.ezzy.core.domain

import java.io.Serializable

class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val email: String? = null,
): Serializable