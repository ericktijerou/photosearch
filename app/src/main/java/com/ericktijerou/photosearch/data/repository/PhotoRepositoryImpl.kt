/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ericktijerou.photosearch.data.repository

import androidx.paging.*
import com.ericktijerou.photosearch.core.DEFAULT_PAGE_SIZE
import com.ericktijerou.photosearch.data.entity.toDomain
import com.ericktijerou.photosearch.data.local.PhotoDataStore
import com.ericktijerou.photosearch.data.local.entity.toDomain
import com.ericktijerou.photosearch.data.remote.PhotoCloudStore
import com.ericktijerou.photosearch.data.remote.SearchPhotoPagingSource
import com.ericktijerou.photosearch.domain.PhotoRepository
import com.ericktijerou.photosearch.domain.entity.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalPagingApi
class PhotoRepositoryImpl(private val remote: PhotoCloudStore, private val local: PhotoDataStore) :
    PhotoRepository {
    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    override fun getPhotoList(query: String): Flow<PagingData<Photo>> {
        val pagingSourceFactory = { local.getPhotoList(query) }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = PhotoRemoteMediator(
                local,
                remote,
                query
            )
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override fun searchPhoto(keyword: String): Flow<PagingData<Photo>> {
        val pagingSourceFactory = { SearchPhotoPagingSource(remote, "search", keyword) }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }
}