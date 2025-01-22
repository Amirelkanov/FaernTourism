package com.example.faerntourism.data.model

import androidx.compose.ui.graphics.painter.Painter


data class CultureArticle(
    val id: Int,
    val img: Painter?,
    val name: String,
    val description: String
)