package com.ezzy.core.interactors

import com.ezzy.core.data.repository.MissingPersonImageRepository

class SaveImage(private val repository: MissingPersonImageRepository) {
    suspend operator fun invoke(
        imageUrl: String
    ) = repository.saveImage(imageUrl)
}

class GetPersonImages(private val repository: MissingPersonImageRepository) {
    suspend operator fun invoke (
        personId: String
    ) = repository.getPersonImages(personId)
}

