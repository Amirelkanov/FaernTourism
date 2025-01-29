package com.amel.faerntourism.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amel.faerntourism.R
import com.amel.faerntourism.ui.theme.AppTypography
import com.amel.faerntourism.ui.theme.FaernTourismTheme

@Composable
fun FaernListItem(
    title: String,
    description: String,
    descriptionMaxLines: Int,
    photoURL: String = "",
    additionalInfo: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = colorScheme.background,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(photoURL)
                    .crossfade(true).build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                error = ColorPainter(colorScheme.primaryContainer),
                placeholder = painterResource(R.drawable.loading_img),
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(5.dp)),
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()

            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .fillMaxWidth(if (trailingContent === null) 1f else 0.7f)
                ) {
                    Text(
                        title,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium, maxLines = 1,
                        color = colorScheme.onSurface, overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        description,
                        style = AppTypography.bodySmall, color = colorScheme.secondary,
                        maxLines = descriptionMaxLines, overflow = TextOverflow.Ellipsis
                    )
                    additionalInfo?.invoke()
                }
                trailingContent?.invoke()
            }
        }
    }
}

@Composable
fun AccountSettingsListItem(
    leadingTitle: String,
    leadingImgVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(
                imageVector = leadingImgVector,
                tint = colorScheme.onErrorContainer,
                contentDescription = null,
                modifier = modifier.size(28.dp)
            )
            Text(
                leadingTitle,
                style = typography.titleLarge
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
            tint = colorScheme.onErrorContainer,
            contentDescription = null
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun TourListItemPreview() {
    FaernTourismTheme {
        FaernListItem(
            "Куртатинское ущелье",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
            additionalInfo = {
                ListItemAdditionalInfo(
                    icon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorScheme.secondary
                        )
                    },
                    text = "от 28.11.2024"
                )
            },
            trailingContent = {
                Text(
                    "1500₽",
                    style = AppTypography.titleMedium,
                    maxLines = 1,
                    color = colorScheme.secondary
                )
            },
            descriptionMaxLines = 2,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun PlaceListItemPreview() {
    FaernTourismTheme {
        FaernListItem(
            "Фатима, держащая Солнце",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et...",
            descriptionMaxLines = 3,
            additionalInfo = {
                ListItemAdditionalInfo(
                    icon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorScheme.secondary
                        )
                    },
                    text = "50 м от вас"
                )
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ArticleListItemPreview() {
    FaernTourismTheme {
        FaernListItem(
            "Кто такой Донбеттыр?",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et...",
            descriptionMaxLines = 4,
            modifier = Modifier.padding(8.dp)
        )
    }
}


@Composable
fun ListItemAdditionalInfo(
    icon: @Composable () -> Unit,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        icon()
        Text(
            text,
            style = AppTypography.labelMedium,
            color = colorScheme.secondary,
            maxLines = 1
        )
    }
}
