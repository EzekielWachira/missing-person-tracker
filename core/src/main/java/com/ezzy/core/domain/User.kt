package com.ezzy.core.domain

import java.io.Serializable

class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val imageSrc: String? = null
): Serializable