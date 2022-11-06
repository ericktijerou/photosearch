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

import com.ericktijerou.photosearch.ui.home.PhotoModelView
import com.google.gson.Gson

sealed class Screen(val route: String) {
    object MainScreen: Screen(route = "main")
    object DetailScreen: Screen(route = "photo/{model}") {
        const val ARG_PHOTO = "model"
        fun route(model: PhotoModelView): String {
            val modelString = Gson().toJson(model)
            return "photo/$modelString"
        }
    }
}