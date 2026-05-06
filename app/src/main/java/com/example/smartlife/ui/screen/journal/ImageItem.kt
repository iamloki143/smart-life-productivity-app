package com.example.smartlife.ui.screen.journal

import android.net.Uri

data class ImageItem (
    val uri: Uri,
    var x : Float=0f,
    var y: Float=0f,
    val scale: Float=1f
)