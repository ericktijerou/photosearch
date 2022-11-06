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
package com.ericktijerou.photosearch.ui.main

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ericktijerou.photosearch.R
import com.ericktijerou.photosearch.ui.component.MenuDrawer
import com.ericktijerou.photosearch.ui.home.HomeScreen
import com.ericktijerou.photosearch.ui.home.PhotoModelView
import com.ericktijerou.photosearch.ui.search.SearchScreen
import com.ericktijerou.photosearch.ui.util.Tab
import com.ericktijerou.photosearch.ui.util.navigateSingleTopTo
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(onPhotoClick: (PhotoModelView) -> Unit) {
    val tabs = listOf(Tab.Search, Tab.Home, Tab.Gallery, Tab.Events, Tab.TheCommons)
    val navController = rememberAnimatedNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentTab = tabs.find { it.route == currentDestination?.route } ?: Tab.Home

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (logo) = createRefs()
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.label_logo),
            modifier = Modifier.constrainAs(logo) {
                end.linkTo(parent.end, margin = 28.dp)
                top.linkTo(parent.top, margin = 28.dp)
            }
        )
        Row(modifier = Modifier.fillMaxSize()) {
            MenuDrawer(
                modifier = Modifier.padding(top = 34.dp),
                selectedTab = currentTab,
                tabs = tabs,
                onTabSelected = { navController.navigateSingleTopTo(it.route) }
            )

            AnimatedNavHost(
                navController = navController,
                startDestination = Tab.Home.route,
            ) {
                composable(
                    route = Tab.Home.route,
                    enterTransition = {
                        tabEnterTransition(AnimatedContentScope.SlideDirection.Start)
                    },
                    exitTransition = {
                        tabExitTransition(AnimatedContentScope.SlideDirection.End)
                    }
                ) {
                    HomeScreen(viewModel = hiltViewModel(), onPhotoClick = onPhotoClick)
                }

                composable(route = Tab.Search.route,
                    enterTransition = {
                        tabEnterTransition(AnimatedContentScope.SlideDirection.Start)
                    },
                    exitTransition = {
                        tabExitTransition(AnimatedContentScope.SlideDirection.End)
                    }
                ) {
                    SearchScreen(viewModel = hiltViewModel(), onPhotoClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.tabExitTransition(
    slideDirection: AnimatedContentScope.SlideDirection,
    duration: Int = 500
) = fadeOut(tween(duration / 2, easing = LinearEasing)) + slideOutOfContainer(
    slideDirection,
    tween(duration, easing = LinearEasing),
    targetOffset = { it / 24 }
)

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.tabEnterTransition(
    slideDirection: AnimatedContentScope.SlideDirection,
    duration: Int = 500,
    delay: Int = duration - 350
) = fadeIn(tween(duration, duration - delay)) + slideIntoContainer(
    slideDirection,
    animationSpec = tween(duration, duration - delay),
    initialOffset = { it / 24 }
)