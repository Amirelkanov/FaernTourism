package com.example.faerntourism.models

import androidx.compose.ui.graphics.painter.Painter
import com.google.android.gms.maps.model.LatLng


data class CultureArticle(
    val id: Int,
    val img: Painter?,
    val name: String,
    val description: String
)