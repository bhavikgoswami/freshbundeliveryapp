package com.witnovus.freshbundeliveryapp.utils.imagepicker.interfaces

import com.witnovus.freshbundeliveryapp.utils.imagepicker.gallery.MediaModel

interface MediaClickInterface {
    fun onMediaClick(media: MediaModel)
    fun onMediaLongClick(media: MediaModel, intentFrom: String)
}