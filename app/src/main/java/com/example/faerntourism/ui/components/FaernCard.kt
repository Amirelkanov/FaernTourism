package com.example.faerntourism.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.faerntourism.R
import com.example.faerntourism.ui.theme.AppTypography
import com.example.faerntourism.ui.theme.FaernTourismTheme


@Composable
fun FaernCard(
    mainTitle: String,
    secondaryTitle: String,
    photoURL: String = "",
    navigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Surface(
        color = colorScheme.background,
        modifier = modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth()
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(photoURL)
                    .crossfade(true).build(),
                contentDescription = mainTitle,
                contentScale = ContentScale.Crop,
                error = ColorPainter(colorScheme.primaryContainer),
                placeholder = painterResource(R.drawable.loading_img),
                modifier = Modifier.fillMaxSize(),
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigateBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = null,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 5.dp)
                ) {
                    Text(
                        text = mainTitle,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 1.em
                    )
                    Text(
                        text = secondaryTitle,
                        color = Color.White.copy(alpha = 0.6f),
                        style = AppTypography.headlineSmall
                    )
                }
            }

        }
    }
}


@Preview(device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Composable
fun FaernCardPreview() {
    FaernTourismTheme {
        FaernCard(
            "Лютеранская Кирха",
            "500 м",
        )
    }
}