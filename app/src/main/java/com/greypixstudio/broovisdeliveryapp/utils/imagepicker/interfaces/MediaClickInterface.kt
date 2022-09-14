package com.greypixstudio.broovisdeliveryapp.utils.imagepicker.interfaces

import com.greypixstudio.broovisdeliveryapp.utils.imagepicker.gallery.MediaModel

interface MediaClickInterface {
    fun onMediaClick(media: MediaModel)
    fun onMediaLongClick(media: MediaModel, intentFrom: String)
}