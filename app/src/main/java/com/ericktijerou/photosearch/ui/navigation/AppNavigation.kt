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
package com.ericktijerou.photosearch.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import com.ericktijerou.photosearch.ui.home.PhotoModelView
import com.ericktijerou.photosearch.ui.main.MainScreen
import com.ericktijerou.photosearch.ui.main.Screen
import com.ericktijerou.photosearch.ui.photo.PhotoScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route,
    ) {
        composable(
            route = Screen.MainScreen.route,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }
        ) {
            MainScreen {
                navController.navigate(Screen.DetailScreen.route(it))
            }
        }

        composable(route = Screen.DetailScreen.route,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }
        ) {
            val modelString = it.arguments?.getString(Screen.DetailScreen.ARG_PHOTO)
            val model = Gson().fromJson(modelString, PhotoModelView::class.java)
            PhotoScreen(model = model, modifier = Modifier.fillMaxSize())

        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.tabExitTransition(
    duration: Int = 500
) = fadeOut(tween(duration / 2, easing = LinearEasing))

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.tabEnterTransition(
    duration: Int = 500,
    delay: Int = duration - 350
) = fadeIn(tween(duration, duration - delay))