package com.example.faerntourism.data.model

import androidx.compose.ui.graphics.painter.Painter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

data class Place(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imgLink: String = "",
    val location: GeoPoint? = null
)