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
package com.ericktijerou.photosearch.domain

import androidx.paging.PagingData
import com.ericktijerou.photosearch.core.ApiException
import com.ericktijerou.photosearch.domain.entity.Photo
import com.ericktijerou.photosearch.domain.usecase.GetRecentPhotosUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetRecentPhotosUseCaseTest {

    private val repository = mockk<PhotoRepository>()
    private val useCase = GetRecentPhotosUseCase(repository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN GetRecentPhotos WHEN repository returns Success THEN returns data`() = runTest {
        val expected: Flow<PagingData<Photo>> =
            flow { emit(PagingData.from(emptyList())) }
        coEvery { repository.getPhotoList(any()) } returns expected
        val output = useCase()
        output.first()
        coVerify { repository.getPhotoList(any()) }
        Assert.assertEquals(1, output.count())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = Throwable::class)
    fun `GIVEN GetRecentPhotos WHEN repository returns Failure THEN throw exception`() = runTest {
        val exception = ApiException()
        coEvery { repository.getPhotoList(any()) } throws exception
        useCase().first()
    }
}