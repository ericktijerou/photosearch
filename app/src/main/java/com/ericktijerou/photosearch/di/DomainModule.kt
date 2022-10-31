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
package com.ericktijerou.photosearch.di

import com.ericktijerou.photosearch.domain.PhotoRepository
import com.ericktijerou.photosearch.domain.usecase.GetRecentPhotosUseCase
import com.ericktijerou.photosearch.domain.usecase.GetTrendingPhotosUseCase
import com.ericktijerou.photosearch.domain.usecase.SearchPhotoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object DomainModule {
    @Provides
    @ActivityRetainedScoped
    fun provideGetTrendingPhotosUseCase(repository: PhotoRepository): GetTrendingPhotosUseCase {
        return GetTrendingPhotosUseCase(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideGetRecentPhotosUseCase(repository: PhotoRepository): GetRecentPhotosUseCase {
        return GetRecentPhotosUseCase(repository)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideSearchPhotoUseCase(repository: PhotoRepository): SearchPhotoUseCase {
        return SearchPhotoUseCase(repository)
    }
}