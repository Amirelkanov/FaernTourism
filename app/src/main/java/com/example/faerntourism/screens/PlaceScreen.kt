import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.faerntourism.ui.theme.FaernTourismTheme

@Composable
fun PlaceScreen(
    placeId: Int?,
    modifier: Modifier = Modifier,
) {
    FaernTourismTheme {
        Scaffold() { padding ->
            Text("Place with id: $placeId", Modifier.padding(padding))
        }
    }
}
