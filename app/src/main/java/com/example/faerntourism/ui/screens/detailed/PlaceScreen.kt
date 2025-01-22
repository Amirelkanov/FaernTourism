package com.example.faerntourism.ui.screens.detailed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.faerntourism.data.places
import com.example.faerntourism.ui.components.DetailedScreenWrapper
import com.example.faerntourism.ui.components.Section
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen(
    placeId: String? = places().first().id.toString(),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val place = places()[placeId?.toInt()!!] // TODO: надо переделывать

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    val titles = listOf("Информация", "Расположение")
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (index != selectedTabIndex) {
                    selectedTabIndex = index
                }
            }
    }

    DetailedScreenWrapper(
        mainCardTitle = place.name,
        secondaryCardTitle = "500 м",
        navigateBack = navigateBack,
        content = {
            Column(modifier) {
                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                coroutineScope.launch {
                                    selectedTabIndex = index
                                    listState.animateScrollToItem(index)
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(10.dp)
                ) {
                    item {
                        Section(
                            title = titles[0],
                            information = {
                                Text(
                                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum aliquam turpis velit, ac ultrices libero vehicula et. Sed ac nibh tellus. Suspendisse congue ac nulla a ultricies. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec quam diam. Mauris in consectetur magna. Nunc pharetra, dui nec molestie fermentum, est est gravida augue, a mattis diam sem at erat. Praesent mattis, leo et euismod condimentum, ante ex ultricies ante, feugiat pulvinar mauris metus at orci. Nullam eu justo eu neque interdum bibendum eu in diam. Donec eu magna non urna pretium tincidunt. Suspendisse eu lorem tincidunt, scelerisque sapien vel, pharetra neque. Integer aliquam condimentum varius.\n" +
                                            "\n" +
                                            "Donec ut egestas arcu. Nullam magna nisl, sollicitudin faucibus arcu eget, condimentum lobortis turpis. Vivamus facilisis pulvinar purus in pulvinar. Maecenas quis dolor sodales, ornare mi eu, mollis elit. Duis eleifend dolor augue, a condimentum elit interdum quis. Pellentesque consectetur, lorem sit amet semper pellentesque, diam enim tristique tortor, at facilisis orci mauris quis sem. Aenean iaculis euismod iaculis. Sed porta hendrerit metus at rhoncus. Suspendisse interdum venenatis odio, et efficitur leo pulvinar nec. Donec nec mauris rutrum, molestie diam id, mollis est. Duis commodo nisi sem, sed egestas quam dictum ac. Nunc ut dignissim ligula. Sed non ex faucibus, rhoncus libero a, tristique erat. Maecenas lobortis sapien sed nibh fringilla, quis egestas diam dictum. In hac habitasse platea dictumst.\n" +
                                            "\n" +
                                            "Aliquam erat volutpat. Maecenas porta leo eget est malesuada pellentesque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam id mi ipsum. Sed pharetra nibh pretium purus ornare rutrum. Quisque ut purus eu diam bibendum molestie. Aenean dapibus tellus quis leo luctus, vel feugiat est malesuada. Duis sed porta quam. Mauris malesuada vel velit sit amet pharetra. Proin egestas turpis est, a luctus ligula pharetra vel. Ut euismod lobortis sapien, ut pharetra eros. Curabitur porttitor interdum elit ut euismod. Fusce sed commodo arcu, sit amet tempus arcu.\n" +
                                            "\n" +
                                            "Morbi ullamcorper velit commodo lectus porta placerat ac sed sem. Donec iaculis nibh tempor, blandit mauris eget, rutrum dolor. Aenean semper lectus justo, sit amet condimentum nisi finibus quis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent euismod lorem felis, a accumsan quam interdum ut. Fusce vel diam et ligula consequat tristique non sit amet eros. Donec efficitur lorem facilisis dolor vestibulum, non sagittis sapien cursus. Praesent viverra scelerisque tortor, lobortis pulvinar lacus commodo nec. Maecenas dictum nisl sed tellus egestas, sit amet tristique diam pulvinar."
                                )
                            }
                        )
                    }

                    // TODO: карту
                    item {
                        Section(
                            title = titles[1],
                            information = {
                                Text(
                                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum aliquam turpis velit, ac ultrices libero vehicula et. Sed ac nibh tellus. Suspendisse congue ac nulla a ultricies. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec quam diam. Mauris in consectetur magna. Nunc pharetra, dui nec molestie fermentum, est est gravida augue, a mattis diam sem at erat. Praesent mattis, leo et euismod condimentum, ante ex ultricies ante, feugiat pulvinar mauris metus at orci. Nullam eu justo eu neque interdum bibendum eu in diam. Donec eu magna non urna pretium tincidunt. Suspendisse eu lorem tincidunt, scelerisque sapien vel, pharetra neque. Integer aliquam condimentum varius.\n" +
                                            "\n" +
                                            "Donec ut egestas arcu. Nullam magna nisl, sollicitudin faucibus arcu eget, condimentum lobortis turpis. Vivamus facilisis pulvinar purus in pulvinar. Maecenas quis dolor sodales, ornare mi eu, mollis elit. Duis eleifend dolor augue, a condimentum elit interdum quis. Pellentesque consectetur, lorem sit amet semper pellentesque, diam enim tristique tortor, at facilisis orci mauris quis sem. Aenean iaculis euismod iaculis. Sed porta hendrerit metus at rhoncus. Suspendisse interdum venenatis odio, et efficitur leo pulvinar nec. Donec nec mauris rutrum, molestie diam id, mollis est. Duis commodo nisi sem, sed egestas quam dictum ac. Nunc ut dignissim ligula. Sed non ex faucibus, rhoncus libero a, tristique erat. Maecenas lobortis sapien sed nibh fringilla, quis egestas diam dictum. In hac habitasse platea dictumst.\n" +
                                            "\n" +
                                            "Aliquam erat volutpat. Maecenas porta leo eget est malesuada pellentesque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam id mi ipsum. Sed pharetra nibh pretium purus ornare rutrum. Quisque ut purus eu diam bibendum molestie. Aenean dapibus tellus quis leo luctus, vel feugiat est malesuada. Duis sed porta quam. Mauris malesuada vel velit sit amet pharetra. Proin egestas turpis est, a luctus ligula pharetra vel. Ut euismod lobortis sapien, ut pharetra eros. Curabitur porttitor interdum elit ut euismod. Fusce sed commodo arcu, sit amet tempus arcu.\n" +
                                            "\n" +
                                            "Morbi ullamcorper velit commodo lectus porta placerat ac sed sem. Donec iaculis nibh tempor, blandit mauris eget, rutrum dolor. Aenean semper lectus justo, sit amet condimentum nisi finibus quis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent euismod lorem felis, a accumsan quam interdum ut. Fusce vel diam et ligula consequat tristique non sit amet eros. Donec efficitur lorem facilisis dolor vestibulum, non sagittis sapien cursus. Praesent viverra scelerisque tortor, lobortis pulvinar lacus commodo nec. Maecenas dictum nisl sed tellus egestas, sit amet tristique diam pulvinar."
                                )
                            }
                        )
                    }
                }
            }
        },
        painterCard = place.img,
    )
}
