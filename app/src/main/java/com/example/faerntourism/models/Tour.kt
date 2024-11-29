package com.example.faerntourism.models

import androidx.compose.ui.graphics.painter.Painter
import com.google.android.gms.maps.model.LatLng

data class Tour (
    val id: Int,
    val img: Painter,
    val name: String,
    val description: String,
    val startDate: String, // TODO: мб какой-нить timestamp
    val price: Int,
    val linkToTour: String
)