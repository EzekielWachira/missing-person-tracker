package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.MissingPersonImageDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPersonImage
import kotlinx.coroutines.flow.Flow

class MissingPersonImageRepository(
    private val dataSource: MissingPersonImageDataSource
) {
    suspend fun saveImage(imageUrl: String) = dataSource.saveImage(imageUrl)
    suspend fun getPersonImages(): Flow<Resource<List<Image>>> =
        dataSource.getPersonImages()
}