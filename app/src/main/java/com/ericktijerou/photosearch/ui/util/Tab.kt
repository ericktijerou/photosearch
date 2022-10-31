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
package com.ericktijerou.photosearch.ui.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.ericktijerou.photosearch.R

sealed class Tab(val route: String, @StringRes val name: Int, val icon: ImageVector) {
    object Home : Tab("home", R.string.label_home, Icons.Outlined.Home)
    object Search : Tab("search", R.string.label_search, Icons.Outlined.Search)
    object TheCommons : Tab("", R.string.label_the_commons, Icons.Outlined.Newspaper)
    object Events : Tab("", R.string.label_events, Icons.Outlined.Event)
    object Gallery : Tab("", R.string.label_gallery, Icons.Outlined.BrowseGallery)
}
