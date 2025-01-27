package com.amel.faerntourism.data.model

import com.google.firebase.firestore.GeoPoint

data class Place(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imgLink: String = "",
    val location: GeoPoint? = null
)