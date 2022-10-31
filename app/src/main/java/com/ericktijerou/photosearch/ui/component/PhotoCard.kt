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
package com.ericktijerou.photosearch.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ericktijerou.photosearch.ui.home.PhotoModelView
import com.ericktijerou.photosearch.ui.theme.AppTheme
import com.ericktijerou.photosearch.ui.util.dpadFocusable
import com.ericktijerou.photosearch.ui.util.recomposeHighlighter
import com.ericktijerou.photosearch.ui.util.verticalGradientScrim

@Composable
fun PhotoCard(
    focused: Boolean,
    photo: PhotoModelView,
    onFocusedFirstTime: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focused) {
        if (focused) {
            onFocusedFirstTime()
            focusRequester.requestFocus()
        }
    }

    val height = 180
    val width = height * 3f / 2

    Column(
        modifier = Modifier
            .widthIn(max = width.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .recomposeHighlighter(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = AppTheme.cardShape,
            modifier = Modifier
                .heightIn(max = height.dp)
                .aspectRatio(3f / 2)
                .focusRequester(focusRequester)
                .dpadFocusable(
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    onFocus = { }
                )
        ) {
            Box {
                AsyncImage(
                    model = photo.url(),
                    contentDescription = photo.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .verticalGradientScrim(
                            color = Color.Black.copy(alpha = 0.5f),
                            startYPercentage = 0f,
                            endYPercentage = 1f
                        )
                        .padding(4.dp)

                ) {
                    Text(
                        text = photo.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )
                    Text(
                        text = photo.owner,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}