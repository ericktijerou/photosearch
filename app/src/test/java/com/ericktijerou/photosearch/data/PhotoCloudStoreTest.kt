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

import com.ericktijerou.photosearch.core.ApiException
import com.ericktijerou.photosearch.core.NotFoundException
import com.ericktijerou.photosearch.data.remote.PhotoCloudStore
import com.ericktijerou.photosearch.data.remote.api.FlickrApi
import com.ericktijerou.photosearch.data.remote.entity.BaseResponse
import com.ericktijerou.photosearch.data.remote.entity.PhotoResponse
import com.ericktijerou.photosearch.mock.PhotoMock.photoResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PhotoCloudStoreTest {

    private val service = mockk<FlickrApi>()
    private val cloudStore = PhotoCloudStore(service)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN CloudStore WHEN getPhotoList THEN return data`() = runTest {
        val expected = BaseResponse<PhotoResponse>().apply { photos = photoResponse }
        coEvery { service.getPhotoList(any(), any(), any(), any()) } returns expected
        val response = cloudStore.getPhotoList(1, 1, "", "")
        coVerify { service.getPhotoList(any(), any(), any(), any()) }
        assertNotNull(response)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = NotFoundException::class)
    fun `GIVEN CloudStore WHEN getPhotoList THEN return not found exception`() = runTest {
        coEvery { service.getPhotoList(any(), any(), any(), any()) } throws NotFoundException()
        val response = cloudStore.getPhotoList(1, 1, "", "")
        coVerify { service.getPhotoList(any(), any(), any(), any()) }
        assertNotNull(response)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = ApiException::class)
    fun `GIVEN CloudStore WHEN getPhotoList THEN return api exception`() = runTest {
        coEvery { service.getPhotoList(any(), any(), any(), any()) } throws ApiException()
        val response = cloudStore.getPhotoList(1, 1, "", "")
        coVerify { service.getPhotoList(any(), any(), any(), any()) }
        assertNotNull(response)
    }
}