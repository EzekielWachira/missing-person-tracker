package com.ezzy.core.interactors

import com.ezzy.core.data.repository.MissingPersonImageRepository
import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import kotlinx.coroutines.flow.Flow

class SaveImage(private val repository: MissingPersonImageRepository) {
    suspend operator fun invoke(
        imageUrl: String
    ) = repository.saveImage(imageUrl)
}

class GetPersonImages(private val repository: MissingPersonImageRepository) {
    suspend operator fun invoke (
        personId: String
    ): Flow<Resource<List<Image>>> = repository.getPersonImages(personId)
}

