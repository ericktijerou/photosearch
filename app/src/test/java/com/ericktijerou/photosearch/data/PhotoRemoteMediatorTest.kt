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
package com.ericktijerou.photosearch.data

import androidx.paging.*
import com.ericktijerou.photosearch.data.entity.PageInfoModel
import com.ericktijerou.photosearch.data.local.PhotoDataStore
import com.ericktijerou.photosearch.data.local.entity.PhotoEntity
import com.ericktijerou.photosearch.data.remote.PhotoCloudStore
import com.ericktijerou.photosearch.data.repository.PhotoRemoteMediator
import io.mockk.*
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoRemoteMediatorTest {
    private val dataStore = mockk<PhotoDataStore>()
    private val cloudStore = mockk<PhotoCloudStore>()

    @OptIn(ExperimentalPagingApi::class)
    private val mediator = PhotoRemoteMediator(dataStore, cloudStore, "", "")

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `GIVEN Mediator WHEN getPhotoList THEN return data`() = runTest {
        val pagingState = PagingState<Int, PhotoEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        coEvery {
            cloudStore.getPhotoList(
                any(),
                any(),
                any(),
                any()
            )
        } returns Pair(PageInfoModel(1, true), listOf())
        coEvery {
            dataStore.doOperationInTransaction(captureCoroutine())
        } coAnswers {
            coroutine<suspend () -> Unit>().coInvoke()
        }
        coEvery { dataStore.clearPhotos("") } just runs
        coEvery { dataStore.getLastPhotoCursor() } returns 1
        coEvery { dataStore.setLastPhotoCursor(1) } just runs
        coEvery { dataStore.savePhotoList(listOf(), "") } just runs
        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}