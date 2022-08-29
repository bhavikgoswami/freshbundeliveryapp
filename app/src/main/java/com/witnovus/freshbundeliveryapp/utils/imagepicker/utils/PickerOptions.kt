package com.witnovus.freshbundeliveryapp.utils.imagepicker.utils

import java.io.Serializable

class PickerOptions : Serializable{

    var maxCount = 1
    var allowFrontCamera = true
    var excludeVideos = false
    var onlyVideo = false
    var maxVideoDuration = 30
    var preSelectedMediaList = ArrayList<String>()

    companion object{
        @JvmStatic
        fun init(): PickerOptions{
            return PickerOptions()
        }
    }
}