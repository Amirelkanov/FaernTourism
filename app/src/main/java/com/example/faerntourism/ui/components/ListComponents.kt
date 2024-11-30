package com.example.faerntourism.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faerntourism.ui.theme.AppTypography
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.example.faerntourism.ui.theme.backgroundLight
import com.example.faerntourism.ui.theme.onSurfaceLight
import com.example.faerntourism.ui.theme.primaryContainerLight
import com.example.faerntourism.ui.theme.secondaryLight

// TODO: favourite
@Composable
fun MyListItem(
    title: String,
    description: String,
    descriptionMaxLines: Int = 2,
    painter: Painter? = null,
    additionalInfo: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = backgroundLight,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Image(
                painter = if (painter !== null) painter else ColorPainter(primaryContainerLight), contentScale = ContentScale.Crop, contentDescription = null,
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
                        .fillMaxWidth(if (trailingContent === null) 1f else 0.8f)
                ) {
                    Text(
                        title,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium, maxLines = 1,
                        color = onSurfaceLight, overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        description,
                        style = AppTypography.bodySmall, color = secondaryLight,
                        maxLines = descriptionMaxLines, overflow = TextOverflow.Ellipsis
                    )
                    additionalInfo?.invoke()
                }
                trailingContent?.invoke()
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun TourListItemPreview() {
    FaernTourismTheme {
        MyListItem(
            "Куртатинское ущелье",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
            additionalInfo = {
                ListItemAdditionalInfo(
                    icon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = secondaryLight
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
                    color = secondaryLight
                )
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun PlaceListItemPreview() {
    FaernTourismTheme {
        MyListItem(
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
                            tint = secondaryLight
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
        MyListItem(
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
            color = secondaryLight,
            maxLines = 1
        )
    }
}
