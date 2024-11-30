package com.example.faerntourism.models

import android.graphics.drawable.Drawable
import android.media.Image
import androidx.compose.ui.graphics.painter.Painter
import com.google.android.gms.maps.model.LatLng

data class Place(
    val id: Int,
    val img: Painter?,
    val name: String,
    val description: String,
    val location: LatLng,
)