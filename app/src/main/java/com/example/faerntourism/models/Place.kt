package com.example.faerntourism.models

import android.media.Image
import com.google.android.gms.maps.model.LatLng

data class Place(
    val id: Int,
    val img: Image?, // Пока хз
    val name: String,
    val description: String, // Мб текст
    val location: LatLng,
)