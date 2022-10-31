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
package com.ericktijerou.photosearch.ui

import androidx.paging.PagingData
import com.ericktijerou.photosearch.domain.entity.Photo
import com.ericktijerou.photosearch.domain.usecase.GetRecentPhotosUseCase
import com.ericktijerou.photosearch.domain.usecase.GetTrendingPhotosUseCase
import com.ericktijerou.photosearch.ui.home.HomeViewModel
import com.ericktijerou.photosearch.ui.home.SectionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HomeViewModelTest {
    private val trendingUseCase = mockk<GetTrendingPhotosUseCase>()
    private val recentUseCase = mockk<GetRecentPhotosUseCase>()
    private val viewModel = HomeViewModel(trendingUseCase, recentUseCase)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN SearchViewModel WHEN trendingUseCase returns Success THEN returns data`() = runTest {
        val expected: Flow<PagingData<Photo>> = flow { emit(PagingData.from(emptyList())) }
        coEvery { trendingUseCase.invoke() } returns expected
        viewModel.getListBySection(SectionType.TRENDING)
        coVerify { trendingUseCase.invoke() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN SearchViewModel WHEN recentUseCase returns Success THEN returns data`() = runTest {
        val expected: Flow<PagingData<Photo>> = flow { emit(PagingData.from(emptyList())) }
        coEvery { recentUseCase.invoke() } returns expected
        viewModel.getListBySection(SectionType.RECENT)
        coVerify { recentUseCase.invoke() }
    }
}