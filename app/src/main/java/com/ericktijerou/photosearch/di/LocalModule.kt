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

import android.content.Context
import com.ericktijerou.photosearch.data.local.PhotoDataStore
import com.ericktijerou.photosearch.data.local.dao.PhotoDao
import com.ericktijerou.photosearch.data.local.system.AppDatabase
import com.ericktijerou.photosearch.data.local.system.PagingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun providePagingManager(@ApplicationContext context: Context) = PagingManager(context)

    @Singleton
    @Provides
    fun providePhotoDao(database: AppDatabase) = database.getPhotoDao()

    @Singleton
    @Provides
    fun providePhotoDataStore(
        dao: PhotoDao,
        appDatabase: AppDatabase,
        pagingManager: PagingManager
    ): PhotoDataStore {
        return PhotoDataStore(dao, appDatabase, pagingManager)
    }
}