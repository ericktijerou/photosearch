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
package com.ericktijerou.photosearch.data.remote.entity

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("pages") var pages: Int? = null,
    @SerializedName("perpage") var perpage: Int? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("photo") var photo: List<PhotoInfo> = listOf()
)