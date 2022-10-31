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

data class PhotoModelView(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val isFamily: Boolean
) {

    // You can see the suffixes here: https://www.flickr.com/services/api/misc.urls.html
    fun url(size: String = ""): String {
        return if (size.isNotEmpty()) {
            "https://live.staticflickr.com/$server/${id}_${secret}_$size.jpg"
        } else {
            // Unique URL format for 500px size
            "https://live.staticflickr.com/$server/${id}_$secret.jpg"
        }
    }
}