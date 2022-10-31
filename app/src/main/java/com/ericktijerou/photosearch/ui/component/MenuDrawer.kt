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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.ericktijerou.photosearch.ui.util.Tab

@Composable
fun MenuDrawer(
    modifier: Modifier = Modifier,
    selectedTab: Tab,
    tabs: List<Tab>,
    onTabSelected: (Tab) -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxHeight()
            .onFocusChanged {
                hasFocus = it.hasFocus
            },
    ) {
        tabs.forEach { tab ->
            CustomTab(
                expand = hasFocus,
                tab = tab,
                selected = selectedTab == tab,
                onTabSelected = {
                    if (tab.route.isNotEmpty()) {
                        onTabSelected(tab)
                        hasFocus = false
                    }
                }
            )
        }
    }
}

@Composable
fun CustomTab(
    tab: Tab,
    selected: Boolean = false,
    expand: Boolean = false,
    modifier: Modifier = Modifier,
    onTabSelected: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isItemFocused by interactionSource.collectIsFocusedAsState()
    val animatedBackground by animateColorAsState(
        animationSpec = tween(500),
        targetValue = when {
            selected && expand -> MaterialTheme.colorScheme.inverseSurface
            isItemFocused -> Color.White.copy(alpha = 0.1f)
            else -> Color.Transparent
        }
    )

    val animatedTextColor by animateColorAsState(
        animationSpec = tween(500),
        targetValue = if (selected) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )

    val animateDpAsState by animateDpAsState(
        targetValue = if (expand) 180.dp else 64.dp, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )

    val iconColor = when {
        selected && expand -> Color.Black
        selected && !expand -> Color.White
        else -> Color.White.copy(alpha = 0.5f)
    }

    Row(
        modifier = modifier
            .background(animatedBackground)
            .width(animateDpAsState)
            .height(48.dp)
            .padding(vertical = 8.dp)
            .selectable(
                selected = selected,
                onClick = onTabSelected,
                enabled = true,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = tab.icon,
            contentDescription = stringResource(id = tab.name),
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (expand) {
            Text(
                text = stringResource(id = tab.name),
                color = animatedTextColor,
                maxLines = 1
            )
        }
    }
}