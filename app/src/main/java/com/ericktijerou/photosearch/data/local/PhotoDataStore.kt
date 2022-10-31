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
package com.ericktijerou.photosearch.data.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.ericktijerou.photosearch.data.entity.PhotoModel
import com.ericktijerou.photosearch.data.entity.toLocal
import com.ericktijerou.photosearch.data.local.dao.PhotoDao
import com.ericktijerou.photosearch.data.local.entity.PhotoEntity
import com.ericktijerou.photosearch.data.local.system.AppDatabase
import com.ericktijerou.photosearch.data.local.system.PagingManager
import javax.inject.Inject

class PhotoDataStore @Inject constructor(
    private val dao: PhotoDao,
    private val database: AppDatabase,
    private val pagingManager: PagingManager
) {
    fun getPhotoList(tag: String): PagingSource<Int, PhotoEntity> {
        return dao.getAll(tag)
    }

    suspend fun savePhotoList(list: List<PhotoModel>, tag: String) {
        dao.insert(*list.map { it.toLocal(tag) }.toTypedArray())
    }

    suspend fun doOperationInTransaction(method: suspend () -> Unit) {
        database.withTransaction {
            method()
        }
    }

    suspend fun clearPhotos(tag: String) {
        dao.clearAll(tag)
    }

    fun getLastPhotoCursor(): Int = pagingManager.lastPhotoCursor

    fun setLastPhotoCursor(value: Int) {
        pagingManager.lastPhotoCursor = value
    }
}