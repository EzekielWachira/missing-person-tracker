package com.ezzy.core.data.datasource

import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Image
import com.ezzy.core.domain.MissingPersonImage
import kotlinx.coroutines.flow.Flow

interface MissingPersonImageDataSource {
    suspend fun saveImage(imageUrl: String){}
    suspend fun getPersonImages(): Flow<Resource<List<Image>>>
}