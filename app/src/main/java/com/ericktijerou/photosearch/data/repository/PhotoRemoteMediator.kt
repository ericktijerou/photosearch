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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ericktijerou.photosearch.core.ApiException
import com.ericktijerou.photosearch.core.orZero
import com.ericktijerou.photosearch.data.local.PhotoDataStore
import com.ericktijerou.photosearch.data.local.entity.PhotoEntity
import com.ericktijerou.photosearch.data.remote.PhotoCloudStore
import java.io.IOException

@ExperimentalPagingApi
class PhotoRemoteMediator(
    private val local: PhotoDataStore,
    private val remote: PhotoCloudStore,
    private val query: String,
    private val keyword: String = ""
) : RemoteMediator<Int, PhotoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        val cursor = when (val pageKeyData = getKeyPageData(loadType)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as? Int
        }
        return try {
            val (pageInfo, userList) = remote.getPhotoList(
                cursor ?: 1,
                state.config.pageSize,
                query,
                keyword
            )
            local.doOperationInTransaction {
                if (loadType == LoadType.REFRESH) local.clearPhotos(query)
                local.setLastPhotoCursor(pageInfo.endCursor)
                local.savePhotoList(userList, query)
            }
            MediatorResult.Success(endOfPaginationReached = !pageInfo.hasNextPage)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: ApiException) {
            MediatorResult.Error(exception)
        }
    }

    private fun getKeyPageData(loadType: LoadType): Any? {
        return when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> local.getLastPhotoCursor()
        }
    }
}