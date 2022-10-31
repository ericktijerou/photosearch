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

import androidx.paging.ExperimentalPagingApi
import com.ericktijerou.photosearch.data.local.PhotoDataStore
import com.ericktijerou.photosearch.data.remote.PhotoCloudStore
import com.ericktijerou.photosearch.data.repository.PhotoRepositoryImpl
import com.ericktijerou.photosearch.mock.PagingSourceTest
import com.ericktijerou.photosearch.mock.PhotoMock.singlePhotoEntity
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class PhotoRepositoryTest {

    private val dataStore = mockk<PhotoDataStore>()
    private val cloudStore = mockk<PhotoCloudStore>()
    private val userRepository = PhotoRepositoryImpl(cloudStore, dataStore)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN Repository WHEN getPhotoList THEN model is filled`() = runTest {
        val expected = PagingSourceTest(data = listOf(singlePhotoEntity, singlePhotoEntity))
        coEvery { dataStore.getPhotoList("") } returns expected
        val response = userRepository.getPhotoList("").first()
        assertNotNull(response)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN Repository WHEN searchPhoto THEN model is filled`() = runTest {
        val response = userRepository.searchPhoto("").first()
        assertNotNull(response)
    }
}