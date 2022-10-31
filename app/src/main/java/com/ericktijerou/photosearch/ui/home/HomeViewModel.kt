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
package com.ericktijerou.photosearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ericktijerou.photosearch.domain.entity.toView
import com.ericktijerou.photosearch.domain.usecase.GetRecentPhotosUseCase
import com.ericktijerou.photosearch.domain.usecase.GetTrendingPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingPhotosUseCase: GetTrendingPhotosUseCase,
    private val getRecentPhotosUseCase: GetRecentPhotosUseCase
) : ViewModel() {
    var isFirstTime: Boolean = true

    private var _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: Flow<State> = _state

    private val popularList: Flow<PagingData<PhotoModelView>> by lazy {
        getTrendingPhotosUseCase().map { pagingData ->
            pagingData.map { it.toView() }
        }.cachedIn(viewModelScope)
    }

    private val recentList: Flow<PagingData<PhotoModelView>> by lazy {
        getRecentPhotosUseCase().map { pagingData ->
            pagingData.map { it.toView() }
        }.cachedIn(viewModelScope)
    }

    fun getListBySection(@SectionType type: Int): Flow<PagingData<PhotoModelView>> {
        return when (type) {
            SectionType.TRENDING -> popularList
            SectionType.RECENT -> recentList
            else -> popularList
        }
    }

    fun updateState() {
        _state.value = State.Loaded
    }

    sealed class State {
        object Loading : State()
        object Loaded : State()
    }
}