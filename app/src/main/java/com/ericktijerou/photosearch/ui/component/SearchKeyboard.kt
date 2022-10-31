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

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.outlined.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ericktijerou.photosearch.R
import com.ericktijerou.photosearch.ui.theme.AppTheme

@Composable
fun SearchKeyboard(modifier: Modifier = Modifier, onKeySelected: (String) -> Unit) {
    Column(modifier = modifier) {
        Controls(onKeySelected)
        AlphaNumericKeyboard(onKeySelected)
    }
}

@Composable
fun Controls(onKeySelected: (String) -> Unit) {
    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(32.dp)
                .width(96.dp)
                .keyboardFocusable(
                    unfocusedBorderColor = Color.Transparent,
                    onFocus = {},
                )
                .clickable {
                    onKeySelected(" ")
                }
                .background(Color.DarkGray)
        ) {
            Icon(
                imageVector = Icons.Outlined.SpaceBar,
                contentDescription = stringResource(id = R.string.label_space),
                modifier = Modifier.width(32.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(32.dp)
                .width(96.dp)
                .keyboardFocusable(
                    unfocusedBorderColor = Color.Transparent,
                    onFocus = {},
                )
                .clickable {
                    onKeySelected("")
                }
                .background(Color.DarkGray)
        ) {
            Icon(
                imageVector = Icons.Filled.Backspace,
                contentDescription = stringResource(id = R.string.label_delete),
                modifier = Modifier.size(32.dp, 16.dp)
            )
        }
    }
}

@Composable
fun AlphaNumericKeyboard(onKeySelected: (String) -> Unit) {
    Row {
        for (i in 0 until 6) {
            Column {
                for (j in 0 until 6) {
                    val offset = i + j * 6
                    val newChar = when {
                        i == 5 && j == 5 -> '0'
                        offset > 25 -> '1' + offset - 26
                        else -> 'a' + offset
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .keyboardFocusable(
                                unfocusedBorderColor = Color.Transparent,
                                onFocus = {},
                            )
                            .clickable {
                                onKeySelected(newChar.toString())
                            }
                            .background(Color.DarkGray)
                    ) {
                        Text(
                            text = newChar.toString(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

fun Modifier.keyboardFocusable(
    unfocusedBorderWidth: Dp = 0.dp,
    focusedBorderWidth: Dp = 2.dp,
    unfocusedBorderColor: Color = Color.Black,
    focusedBorderColor: Color = Color.White,
    onFocus: (FocusState) -> Unit
) = composed {
    val boxInteractionSource = remember { MutableInteractionSource() }
    val isItemFocused by boxInteractionSource.collectIsFocusedAsState()

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isItemFocused) focusedBorderWidth else unfocusedBorderWidth
    )

    val infiniteTransition = rememberInfiniteTransition()
    val animatedFocusedBorder by infiniteTransition.animateColor(
        initialValue = focusedBorderColor.copy(alpha = 0.5f),
        targetValue = focusedBorderColor,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isItemFocused) animatedFocusedBorder else unfocusedBorderColor
    )

    this
        .onFocusChanged { onFocus(it) }
        .focusable(interactionSource = boxInteractionSource)
        .padding(1.dp)
        .border(
            width = animatedBorderWidth,
            color = animatedBorderColor,
            shape = AppTheme.cardShape
        )
}

