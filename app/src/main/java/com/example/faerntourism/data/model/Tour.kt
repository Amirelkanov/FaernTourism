package com.example.faerntourism.data.model

import androidx.compose.ui.graphics.painter.Painter

data class Tour (
    val id: Int,
    val img: Painter?,
    val name: String,
    val description: String,
    val startDate: String, // TODO: мб какой-нить timestamp
    val price: Int,
    val linkToTour: String
)