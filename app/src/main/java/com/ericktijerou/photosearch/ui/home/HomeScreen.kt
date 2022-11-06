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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import com.ericktijerou.photosearch.ui.component.LoadingItem
import com.ericktijerou.photosearch.ui.component.PhotoCard
import com.ericktijerou.photosearch.ui.util.defaultSectionList
import com.ericktijerou.photosearch.ui.util.itemsIndexed

@Composable
fun HomeScreen(viewModel: HomeViewModel, onPhotoClick: (PhotoModelView) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        SectionList(viewModel, defaultSectionList, onPhotoClick = onPhotoClick)
    }
}

@Composable
fun SectionList(
    homeViewModel: HomeViewModel,
    list: List<PhotoSection>,
    onPhotoClick: (PhotoModelView) -> Unit
) {
    TvLazyColumn(
        contentPadding = PaddingValues(vertical = 27.dp),
        pivotOffsets = PivotOffsets(
            parentFraction = 0.15f,
        )
    ) {
        items(list) { section ->
            CardRow(
                homeViewModel = homeViewModel,
                photoSection = section,
                contentPadding = PaddingValues(horizontal = 16.dp),
                onPhotoClick = onPhotoClick
            )
        }
    }
}

@Composable
fun CardRow(
    homeViewModel: HomeViewModel,
    photoSection: PhotoSection,
    contentPadding: PaddingValues,
    onPhotoClick: (PhotoModelView) -> Unit
) {
    val lazyItems = homeViewModel.getListBySection(photoSection.type).collectAsLazyPagingItems()
    Column {
        Text(
            text = stringResource(id = photoSection.name),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(contentPadding)
        )
        TvLazyRow(
            modifier = Modifier.heightIn(min = 180.dp),
            contentPadding = contentPadding,
            pivotOffsets = PivotOffsets(
                parentFraction = 0.05f,
            )
        ) {
            itemsIndexed(lazyItems) { index, photo ->
                photo?.let {
                    PhotoCard(
                        photo = it,
                        focused = index == 0 && homeViewModel.isFirstTime,
                        onFocusedFirstTime = { homeViewModel.isFirstTime = false },
                        onPhotoClick = onPhotoClick
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            lazyItems.apply {
                if (loadState.append is LoadState.Loading) {
                    item { LoadingItem() }
                }
            }
        }
    }
}