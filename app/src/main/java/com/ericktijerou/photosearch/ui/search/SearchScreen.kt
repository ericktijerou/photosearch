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

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import com.ericktijerou.photosearch.R
import com.ericktijerou.photosearch.ui.component.LoadingItem
import com.ericktijerou.photosearch.ui.component.LoadingView
import com.ericktijerou.photosearch.ui.component.PhotoCard
import com.ericktijerou.photosearch.ui.component.SearchKeyboard
import com.ericktijerou.photosearch.ui.home.PhotoModelView
import com.ericktijerou.photosearch.ui.util.itemsIndexed

@Composable
fun SearchScreen(viewModel: SearchViewModel, onPhotoClick: (PhotoModelView) -> Unit) {
    var text by remember { mutableStateOf("") }
    viewModel.search(text)
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        SearchKeyboard(modifier = Modifier.padding(vertical = 40.dp, horizontal = 16.dp)) {
            when {
                it.isEmpty() && text.isNotEmpty() -> text = text.substring(0, text.length - 1)
                else -> text += it
            }
        }
        val titleSearch = text.ifEmpty { stringResource(id = R.string.label_most_searched) }
        Column(modifier = Modifier.padding(top = 28.dp, start = 16.dp, end = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(id = R.string.label_search),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(16.dp)
                    )
                }
                Text(text = titleSearch)
            }
            ResultListGrid(
                viewModel = viewModel,
                contentPadding = PaddingValues(horizontal = 0.dp),
                onPhotoClick = onPhotoClick
            )
        }
    }
}


@Composable
fun ResultListGrid(
    viewModel: SearchViewModel,
    contentPadding: PaddingValues,
    onPhotoClick: (PhotoModelView) -> Unit
) {
    val lazyItems = viewModel.searchResult.collectAsLazyPagingItems()
    Column {
        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(3),
            contentPadding = contentPadding,
            pivotOffsets = PivotOffsets(
                parentFraction = 0.05f,
            )
        ) {
            itemsIndexed(lazyItems) { _, photo ->
                photo?.let {
                    PhotoCard(
                        photo = it,
                        focused = false,
                        onFocusedFirstTime = { },
                        onPhotoClick = onPhotoClick
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(4.dp)) }

            lazyItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingView(modifier = Modifier.fillMaxSize()) }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                }
            }
        }
    }
}
