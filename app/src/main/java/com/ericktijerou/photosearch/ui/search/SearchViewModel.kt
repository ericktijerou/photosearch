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
package com.ericktijerou.photosearch.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ericktijerou.photosearch.domain.entity.toView
import com.ericktijerou.photosearch.domain.usecase.SearchPhotoUseCase
import com.ericktijerou.photosearch.ui.home.PhotoModelView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchPhotoUseCase: SearchPhotoUseCase) :
    ViewModel() {

    private var _searchResult: MutableStateFlow<PagingData<PhotoModelView>> =
        MutableStateFlow(PagingData.empty())

    val searchResult: Flow<PagingData<PhotoModelView>> = _searchResult

    var job: Job? = null

    fun search(keyword: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val results = searchPhotoUseCase(keyword).map { pagingData ->
                pagingData.map { it.toView() }
            }.cachedIn(viewModelScope)
            _searchResult.value = results.first()
        }
    }
}