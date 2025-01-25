package com.example.faerntourism.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.faerntourism.R
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.ramani.compose.CameraPosition
import org.ramani.compose.MapLibre
import org.ramani.compose.Symbol
import org.ramani.compose.UiSettings

@Composable
fun FaernMap(latitude: Double, longitude: Double) {
    var showMap by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe when the screen goes to background or navigates away
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> showMap = false
                Lifecycle.Event.ON_RESUME -> showMap = true
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (showMap) {
        val targetCoordinates = LatLng(latitude, longitude)

        var cameraPosition by rememberSaveable {
            mutableStateOf(
                CameraPosition(
                    target = targetCoordinates, zoom = 15.0
                )
            )
        }


        val styleUrl =
            "https://tiles.stadiamaps.com/styles/alidade_smooth${if (isSystemInDarkTheme()) "_dark" else ""}.json?api_key=${
                stringResource(
                    R.string.maplibre_style_api_key
                )
            }"

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            MapLibre(
                modifier = Modifier.fillMaxSize(),
                styleBuilder = Style.Builder().fromUri(styleUrl),
                cameraPosition = cameraPosition,
                uiSettings = UiSettings(isLogoEnabled = false)
            ) {
                Symbol(center = targetCoordinates)
            }

            ZoomControls(onPlusClick = {
                cameraPosition = CameraPosition(
                    target = targetCoordinates, zoom = cameraPosition.zoom?.plus(1.0)
                )
            }, onMinusClick = {
                cameraPosition = CameraPosition(
                    target = targetCoordinates, zoom = cameraPosition.zoom?.minus(1.0)
                )
            }, modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun ZoomControls(
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        FloatingActionButton(
            onClick = onPlusClick,
            modifier = Modifier.size(40.dp),
        ) {
            Text("+")
        }
        FloatingActionButton(
            onClick = onMinusClick,
            modifier = Modifier.size(40.dp),
        ) {
            Text("-")
        }

    }
}
