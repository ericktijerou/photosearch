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
package com.ericktijerou.photosearch.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ericktijerou.photosearch.data.entity.PhotoModel
import com.ericktijerou.photosearch.data.remote.entity.toData
import retrofit2.HttpException
import java.io.IOException

class SearchPhotoPagingSource(
    private val cloudStore: PhotoCloudStore,
    private val query: String,
    private val keyword: String
) : PagingSource<Int, PhotoModel>() {

    override fun getRefreshKey(state: PagingState<Int, PhotoModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        val pageIndex = params.key ?: INITIAL_PAGE_INDEX
        return try {
            val response = cloudStore.getPhotoResponse(query, pageIndex, PAGE_SIZE, keyword)
            val photoList = response.photo.map { it.toData() }
            val nextKey = if (photoList.isEmpty() || photoList.size < 20) {
                null
            } else {
                pageIndex + 1
            }
            LoadResult.Page(
                data = photoList,
                prevKey = if (pageIndex == INITIAL_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val INITIAL_PAGE_INDEX = 1
        private const val PAGE_SIZE = 20
    }
}