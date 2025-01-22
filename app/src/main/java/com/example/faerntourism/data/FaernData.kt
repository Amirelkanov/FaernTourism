package com.example.faerntourism.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.faerntourism.R
import com.example.faerntourism.data.model.CultureArticle
import com.example.faerntourism.data.model.Place
import com.example.faerntourism.data.model.Tour
import com.google.android.gms.maps.model.LatLng


/**
 * Hardcoded data for app
 */

@Composable
fun places() = listOf(
    Place(
        1,
        painterResource(R.drawable.fatima_holding_sun_monument),
        "Фатима, держащая Солнце",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum aliquam turpis velit, ac ultrices libero vehicula et. Sed ac nibh tellus. Suspendisse congue ac nulla a ultricies. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec quam diam. Mauris in consectetur magna. Nunc pharetra, dui nec molestie fermentum, est est gravida augue, a mattis diam sem at erat. Praesent mattis, leo et euismod condimentum, ante ex ultricies ante, feugiat pulvinar mauris metus at orci. Nullam eu justo eu neque interdum bibendum eu in diam. Donec eu magna non urna pretium tincidunt. Suspendisse eu lorem tincidunt, scelerisque sapien vel, pharetra neque. Integer aliquam condimentum varius.\n" +
                "\n" +
                "Donec ut egestas arcu. Nullam magna nisl, sollicitudin faucibus arcu eget, condimentum lobortis turpis. Vivamus facilisis pulvinar purus in pulvinar. Maecenas quis dolor sodales, ornare mi eu, mollis elit. Duis eleifend dolor augue, a condimentum elit interdum quis. Pellentesque consectetur, lorem sit amet semper pellentesque, diam enim tristique tortor, at facilisis orci mauris quis sem. Aenean iaculis euismod iaculis. Sed porta hendrerit metus at rhoncus. Suspendisse interdum venenatis odio, et efficitur leo pulvinar nec. Donec nec mauris rutrum, molestie diam id, mollis est. Duis commodo nisi sem, sed egestas quam dictum ac. Nunc ut dignissim ligula. Sed non ex faucibus, rhoncus libero a, tristique erat. Maecenas lobortis sapien sed nibh fringilla, quis egestas diam dictum. In hac habitasse platea dictumst.\n" +
                "\n" +
                "Aliquam erat volutpat. Maecenas porta leo eget est malesuada pellentesque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam id mi ipsum. Sed pharetra nibh pretium purus ornare rutrum. Quisque ut purus eu diam bibendum molestie. Aenean dapibus tellus quis leo luctus, vel feugiat est malesuada. Duis sed porta quam. Mauris malesuada vel velit sit amet pharetra. Proin egestas turpis est, a luctus ligula pharetra vel. Ut euismod lobortis sapien, ut pharetra eros. Curabitur porttitor interdum elit ut euismod. Fusce sed commodo arcu, sit amet tempus arcu.\n" +
                "\n" +
                "Morbi ullamcorper velit commodo lectus porta placerat ac sed sem. Donec iaculis nibh tempor, blandit mauris eget, rutrum dolor. Aenean semper lectus justo, sit amet condimentum nisi finibus quis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent euismod lorem felis, a accumsan quam interdum ut. Fusce vel diam et ligula consequat tristique non sit amet eros. Donec efficitur lorem facilisis dolor vestibulum, non sagittis sapien cursus. Praesent viverra scelerisque tortor, lobortis pulvinar lacus commodo nec. Maecenas dictum nisl sed tellus egestas, sit amet tristique diam pulvinar.",
        LatLng(43.075616, 44.656784)
    ),
    Place(
        2,
        painterResource(R.drawable.philharmonic),
        "Лютеранская Кирха",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(43.036756, 44.677827)
    ),
    Place(
        3,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(42.858264, 44.305158)
    ),
    Place(
        4,
        painterResource(R.drawable.sogdiana_cafe),
        "Кафе Согдиана",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.667506, 42.984168)
    ),
    Place(
        5,
        painterResource(R.drawable.saint_bogoroditsa_church_1),
        "Церковь Рождества Богородицы",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.686002, 43.019957)
    ),
    Place(
        6,
        painterResource(R.drawable.vachtangovs_house_museum),
        "Дом Евгения Вахтангова",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.681546, 43.021901)
    ),
    Place(
        7,
        painterResource(R.drawable.saint_bogoroditsa_church_1),
        "Церковь Рождества Богородицы",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.686002, 43.019957)
    )
)

@Composable
fun tours() = listOf(
    Tour(
        1,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
    Tour(
        2,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
    Tour(
        3,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
    Tour(
        4,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
    Tour(
        5,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
    Tour(
        6,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
    Tour(
        7,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed",
        "28.11.2024",
        1500,
        "google.com"
    ),
)

@Composable
fun cultureArticles() = listOf(
    CultureArticle(
        1,
        null,
        "Кто такой Донбеттыр?",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum aliquam turpis velit, ac ultrices libero vehicula et. Sed ac nibh tellus. Suspendisse congue ac nulla a ultricies. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec quam diam. Mauris in consectetur magna. Nunc pharetra, dui nec molestie fermentum, est est gravida augue, a mattis diam sem at erat. Praesent mattis, leo et euismod condimentum, ante ex ultricies ante, feugiat pulvinar mauris metus at orci. Nullam eu justo eu neque interdum bibendum eu in diam. Donec eu magna non urna pretium tincidunt. Suspendisse eu lorem tincidunt, scelerisque sapien vel, pharetra neque. Integer aliquam condimentum varius.\n" +
                "\n" +
                "Donec ut egestas arcu. Nullam magna nisl, sollicitudin faucibus arcu eget, condimentum lobortis turpis. Vivamus facilisis pulvinar purus in pulvinar. Maecenas quis dolor sodales, ornare mi eu, mollis elit. Duis eleifend dolor augue, a condimentum elit interdum quis. Pellentesque consectetur, lorem sit amet semper pellentesque, diam enim tristique tortor, at facilisis orci mauris quis sem. Aenean iaculis euismod iaculis. Sed porta hendrerit metus at rhoncus. Suspendisse interdum venenatis odio, et efficitur leo pulvinar nec. Donec nec mauris rutrum, molestie diam id, mollis est. Duis commodo nisi sem, sed egestas quam dictum ac. Nunc ut dignissim ligula. Sed non ex faucibus, rhoncus libero a, tristique erat. Maecenas lobortis sapien sed nibh fringilla, quis egestas diam dictum. In hac habitasse platea dictumst.\n" +
                "\n" +
                "Aliquam erat volutpat. Maecenas porta leo eget est malesuada pellentesque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam id mi ipsum. Sed pharetra nibh pretium purus ornare rutrum. Quisque ut purus eu diam bibendum molestie. Aenean dapibus tellus quis leo luctus, vel feugiat est malesuada. Duis sed porta quam. Mauris malesuada vel velit sit amet pharetra. Proin egestas turpis est, a luctus ligula pharetra vel. Ut euismod lobortis sapien, ut pharetra eros. Curabitur porttitor interdum elit ut euismod. Fusce sed commodo arcu, sit amet tempus arcu.\n" +
                "\n" +
                "Morbi ullamcorper velit commodo lectus porta placerat ac sed sem. Donec iaculis nibh tempor, blandit mauris eget, rutrum dolor. Aenean semper lectus justo, sit amet condimentum nisi finibus quis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent euismod lorem felis, a accumsan quam interdum ut. Fusce vel diam et ligula consequat tristique non sit amet eros. Donec efficitur lorem facilisis dolor vestibulum, non sagittis sapien cursus. Praesent viverra scelerisque tortor, lobortis pulvinar lacus commodo nec. Maecenas dictum nisl sed tellus egestas, sit amet tristique diam pulvinar."
    ),
    CultureArticle(
        2, null, "Кто такой Донбеттыр?", "Lorem ipsum dolor sit amet, consectetur\n" +
                "adipiscing elit, sed do eiusmod tempor\n" +
                "incididunt ut labore et dolore magna\n" +
                "aliqua"
    ),
    CultureArticle(
        3, null, "Кто такой Донбеттыр?", "Lorem ipsum dolor sit amet, consectetur\n" +
                "adipiscing elit, sed do eiusmod tempor\n" +
                "incididunt ut labore et dolore magna\n" +
                "aliqua"
    ),
    CultureArticle(
        4, null, "Кто такой Донбеттыр?", "Lorem ipsum dolor sit amet, consectetur\n" +
                "adipiscing elit, sed do eiusmod tempor\n" +
                "incididunt ut labore et dolore magna\n" +
                "aliqua"
    ),
    CultureArticle(
        5, null, "Кто такой Донбеттыр?", "Lorem ipsum dolor sit amet, consectetur\n" +
                "adipiscing elit, sed do eiusmod tempor\n" +
                "incididunt ut labore et dolore magna\n" +
                "aliqua"
    ),
    CultureArticle(
        6, null, "Кто такой Донбеттыр?", "Lorem ipsum dolor sit amet, consectetur\n" +
                "adipiscing elit, sed do eiusmod tempor\n" +
                "incididunt ut labore et dolore magna\n" +
                "aliqua"
    ),
)
