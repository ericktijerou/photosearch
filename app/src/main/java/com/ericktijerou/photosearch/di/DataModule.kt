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

import androidx.paging.ExperimentalPagingApi
import com.ericktijerou.photosearch.data.local.PhotoDataStore
import com.ericktijerou.photosearch.data.remote.PhotoCloudStore
import com.ericktijerou.photosearch.data.repository.PhotoRepositoryImpl
import com.ericktijerou.photosearch.domain.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataModule {
    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @ActivityRetainedScoped
    fun providePhotoRepository(
        dataStore: PhotoDataStore,
        cloudStore: PhotoCloudStore
    ): PhotoRepository {
        return PhotoRepositoryImpl(cloudStore, dataStore)
    }
}