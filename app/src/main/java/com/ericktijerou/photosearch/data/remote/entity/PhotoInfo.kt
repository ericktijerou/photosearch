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

import com.ericktijerou.photosearch.core.orZero
import com.ericktijerou.photosearch.core.toBoolean
import com.ericktijerou.photosearch.data.entity.PhotoModel
import com.google.gson.annotations.SerializedName

data class PhotoInfo(
    @SerializedName("id") var id: String? = null,
    @SerializedName("owner") var owner: String? = null,
    @SerializedName("secret") var secret: String? = null,
    @SerializedName("server") var server: String? = null,
    @SerializedName("farm") var farm: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("ispublic") var isPublic: Int? = null,
    @SerializedName("isfriend") var isFriend: Int? = null,
    @SerializedName("isfamily") var isFamily: Int? = null
)

fun PhotoInfo.toData(): PhotoModel {
    return PhotoModel(
        id = id.orEmpty(),
        owner = owner.orEmpty(),
        secret = secret.orEmpty(),
        server = server.orEmpty(),
        farm = farm.orZero(),
        title = title.orEmpty(),
        isPublic = isPublic.orZero().toBoolean(),
        isFriend = isFriend.orZero().toBoolean(),
        isFamily = isFamily.orZero().toBoolean()
    )
}