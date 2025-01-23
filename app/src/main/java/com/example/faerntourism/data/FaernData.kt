package com.example.faerntourism.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.faerntourism.R
import com.example.faerntourism.data.model.Article
import com.example.faerntourism.data.model.Place
import com.example.faerntourism.data.model.Tour
import com.google.firebase.firestore.GeoPoint


/**
 * Hardcoded data for app
 */

@Composable
fun places() = listOf(
    Place(
        id = "1",
        name = "Sunny Beach",
        description = "A beautiful beach with golden sands and clear blue waters.",
        imgLink = "https://example.com/images/sunny_beach.jpg",
        location = GeoPoint(42.698334, 27.710271) // Example: Coordinates of a beach
    ),
    Place(
        id = "2",
        name = "Mountain Retreat",
        description = "A peaceful retreat in the heart of the mountains.",
        imgLink = "https://example.com/images/mountain_retreat.jpg",
        location = GeoPoint(46.818188, 8.227512) // Example: Coordinates of Swiss Alps
    ),
    Place(
        id = "3",
        name = "City Park",
        description = "A vibrant city park with walking trails and picnic spots.",
        imgLink = "https://example.com/images/city_park.jpg",
        location = GeoPoint(40.712776, -74.005974) // Example: New York Central Park
    ),
    Place(
        id = "4",
        name = "Historic Castle",
        description = "An ancient castle with rich history and stunning views.",
        imgLink = "https://example.com/images/historic_castle.jpg",
        location = GeoPoint(48.856613, 2.352222) // Example: Coordinates in Paris
    ),
    Place(
        id = "5",
        name = "Desert Oasis",
        description = "A serene oasis in the middle of a vast desert.",
        imgLink = "https://example.com/images/desert_oasis.jpg",
        location = GeoPoint(25.276987, 55.296249) // Example: Coordinates of a desert oasis
    )
)

@Composable
fun tours() = listOf(
    Tour(
        name = "Beauty of the Mountains",
        price = "53,400 руб.",
        date = "24 January 2025",
        link = "https://trip-kavkaz.com/tour/1",
        imgLink = "https://trip-kavkaz.com/images/tour1.jpg",
        description = "Explore the majestic mountains and rich cultural heritage of the Caucasus in this immersive 8-day tour."
    ),
    Tour(
        name = "Cultural Wonders of the Caucasus",
        price = "45,200 руб.",
        date = "15 February 2025",
        link = "https://trip-kavkaz.com/tour/2",
        imgLink = "https://trip-kavkaz.com/images/tour2.jpg",
        description = "Discover the unique traditions and stunning landscapes of Dagestan and Ingushetia in this guided tour."
    ),
    Tour(
        name = "Historical Landmarks and Nature",
        price = "62,000 руб.",
        date = "10 March 2025",
        link = "https://trip-kavkaz.com/tour/3",
        imgLink = "https://trip-kavkaz.com/images/tour3.jpg",
        description = "A perfect blend of history and nature, this tour takes you to ancient sites and breathtaking canyons."
    ),
    Tour(
        name = "The Charm of North Ossetia",
        price = "39,800 руб.",
        date = "5 April 2025",
        link = "https://trip-kavkaz.com/tour/4",
        imgLink = "https://trip-kavkaz.com/images/tour4.jpg",
        description = "Experience the charm of North Ossetia with its scenic landscapes, cultural richness, and traditional cuisine."
    ),
    Tour(
        name = "Caucasus Highlights",
        price = "50,000 руб.",
        date = "20 May 2025",
        link = "https://trip-kavkaz.com/tour/5",
        imgLink = "https://trip-kavkaz.com/images/tour5.jpg",
        description = "From the peaks of Elbrus to the historical sites of Derbent, this tour showcases the best of the Caucasus."
    )
)

@Composable
fun articles() = listOf(
    Article(
        id = "1",
        imgLink = "https://example.com/images/article1.jpg",
        name = "The Art of Pottery",
        description = "Explore the intricate art of pottery and its cultural significance through the ages."
    ),
    Article(
        id = "2",
        imgLink = "https://example.com/images/article2.jpg",
        name = "Traditional Dance Forms",
        description = "A deep dive into the world of traditional dances that tell stories of heritage and tradition."
    ),
    Article(
        id = "3",
        imgLink = "https://example.com/images/article3.jpg",
        name = "Ancient Architecture",
        description = "Discover the marvels of ancient architecture and the stories they tell about civilizations."
    ),
    Article(
        id = "4",
        imgLink = "https://example.com/images/article4.jpg",
        name = "Culinary Heritage",
        description = "A journey through the flavors and recipes passed down through generations."
    ),
    Article(
        id = "5",
        imgLink = "https://example.com/images/article5.jpg",
        name = "Folklore and Mythology",
        description = "Unravel the tales and myths that shaped the cultural fabric of societies."
    )
)

