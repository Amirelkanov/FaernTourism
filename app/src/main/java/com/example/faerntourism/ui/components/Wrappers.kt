package com.example.faerntourism.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.faerntourism.R
import com.example.faerntourism.ui.theme.AppTypography
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.example.faerntourism.ui.theme.backgroundLight
import com.example.faerntourism.ui.theme.onSurfaceLight


@Composable
fun GeneralScreenWrapper(
    topAppBarTitle: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(color = backgroundLight)
            ) {
                Text(
                    topAppBarTitle,
                    fontSize = 39.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = modifier.padding(horizontal = 5.dp)
                )
            }
        },
        bottomBar = { FaernBottomNavigation() },
        containerColor = backgroundLight
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}


@Composable
fun DetailedScreenWrapper(
    mainCardTitle: String,
    secondaryCardTitle: String,
    painterCard: Painter? = null,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        MyCard(
            mainCardTitle,
            secondaryCardTitle,
            painterCard
        )
    }) { contentPadding ->
        Box(modifier = modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun Section(
    title: String,
    information: @Composable () -> Unit,
    actionButton: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, fontSize = 32.sp, fontWeight = FontWeight.Medium, color = onSurfaceLight)
            actionButton?.invoke()
        }
        information()
    }
}

@Preview(heightDp = 891, widthDp = 411)
@Composable
fun GeneralScreenWrapperPreview() {
    FaernTourismTheme {
        GeneralScreenWrapper("Туры", content = {
            ScrollContent()
        })
    }
}

@Preview(heightDp = 891, widthDp = 411)
@Composable
fun DetailedScreenWrapperPreview() {
    FaernTourismTheme {
        DetailedScreenWrapper(
            "Лютеранская Кирха",
            "500 м",
            painterCard = painterResource(R.drawable.philharmonic),
            content = { ScrollContent() }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun SectionPreview() {
    FaernTourismTheme {
        Section(
            title = "Информация",
            information = {
                Text(
                    "Lorem ipsum dolor sit amet, consectetur\n" +
                            "adipiscing elit, sed do eiusmod tempor incididunt\n" +
                            "ut labore et dolore magna aliqua. Ut enim ad\n" +
                            "minim veniam, quis nostrud exercitation ullamco\n" +
                            "laboris nisi ut aliquip ex ea commodo consequat.\n" +
                            "Duis aute irure dolor in reprehenderit in voluptate\n" +
                            "velit esse cillum dolore eu fugiat nulla pariatur. \n" +
                            "\n" +
                            "Duis torquent himenaeos quisque elementum\n" +
                            "eros lobortis elit. Dis nunc lectus accumsan\n" +
                            "elementum a blandit lectus dignissim at. Aut\n" +
                            "dapibus torquent class enim montes himenaeos.\n" +
                            "Pellentesque tempus fusce fames purus semper\n" +
                            "lorem? Feugiat curae aenean arcu aliquet\n" +
                            "imperdiet. Parturient feugiat dictumst metus semper sollicitudin congue. Aliquam nibh nisi\n" +
                            "dignissim egestas duis habitasse.",
                    style = AppTypography.bodyLarge,
                    color = onSurfaceLight
                )
            },
            actionButton = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}


@Composable
private fun ScrollContent() {
    val range = 1..100

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(range.count()) { index ->
            Text(text = "- List item number ${index + 1}")
        }
    }
}
