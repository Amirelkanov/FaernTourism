package com.amel.faerntourism.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.amel.faerntourism.R
import com.amel.faerntourism.ui.theme.FaernTourismTheme

@Composable
fun SearchBar(
    state: MutableState<TextFieldValue>,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            trailingContent?.invoke()
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                stringResource(R.string.placeholder_search),
                fontSize = 20.sp
            )
        },
        shape = MaterialTheme.shapes.extraLarge,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorScheme.surfaceContainerHigh,
            focusedContainerColor = colorScheme.surfaceContainerHigh,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = colorScheme.onSurface, fontSize = 20.sp
        )
    )
}


@SuppressLint("UnrememberedMutableState") // Тут стейт просто как заглушка все равно
@Preview(showBackground = true, backgroundColor = 0xFFFFF8EF)
@Composable
fun SearchBarPreview() {
    FaernTourismTheme {

        SearchBar(mutableStateOf(TextFieldValue()))
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, backgroundColor = 0xFFFFF8EF)
@Composable
fun SearchBarWithTrailingIconPreview() {
    FaernTourismTheme {
        SearchBar(mutableStateOf(TextFieldValue()), trailingContent = {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
            )
        })
    }
}

