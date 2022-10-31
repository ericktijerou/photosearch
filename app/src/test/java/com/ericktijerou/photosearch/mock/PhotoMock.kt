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
package com.ericktijerou.photosearch.mock

import com.ericktijerou.photosearch.data.entity.PhotoModel
import com.ericktijerou.photosearch.data.local.entity.PhotoEntity
import com.ericktijerou.photosearch.data.remote.entity.PhotoInfo
import com.ericktijerou.photosearch.data.remote.entity.PhotoResponse
import com.ericktijerou.photosearch.domain.entity.Photo

object PhotoMock {
    val singlePhotoModel = PhotoModel("", "", "", "", 0, "", false, false, false)
    val singlePhotoEntity = PhotoEntity("", "", "", "", 0, "", false, false, false, "")
    val singlePhoto = Photo("", "", "", "", 0, "", false, false, false)
    val singlePhotoInfo = PhotoInfo("", "", "", "", 0, "", 1, 1, 1)
    val photoResponse = PhotoResponse(1, 10, 2, 40, listOf(singlePhotoInfo, singlePhotoInfo))
}