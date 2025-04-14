package com.brunodegan.pokedex.ui.screen.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.ui.ErrorUiState
import com.brunodegan.pokedex.base.ui.LoaderUiState
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.data.metrics.TrackScreen
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import com.brunodegan.pokedex.ui.screen.details.viewModel.PokemonViewModelDetails
import org.koin.androidx.compose.koinViewModel

private const val SCREEN_NAME = "Details"

@Composable
fun DetailsScreen(
    id: String?,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: PokemonViewModelDetails = koinViewModel()
) {
    val errorMessage = stringResource(R.string.http_response_generic_error_message)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrackScreen(screenName = SCREEN_NAME)

    BackHandler {
        onNavigateUp()
    }

    LaunchedEffect(id) {
        viewModel.getPokemonDetails(id = id, errorMessage = errorMessage)
    }

    DetailsScreen(
        id = id, onShowSnackbar = onShowSnackbar, onEvent = { event ->
            viewModel.onEvent(event)
        }, viewModel = viewModel, uiState = uiState, modifier = modifier
    )
}

@Composable
private fun DetailsScreen(
    modifier: Modifier = Modifier,
    id: String?,
    onEvent: (PokemonDetailsUiEvents) -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: PokemonViewModelDetails,
    uiState: PokemonDetailsUiState
) {
    SnackbarUiState(
        onSnackbarEvent = onShowSnackbar, viewModel = viewModel
    )

    HandleUiState(uiState = uiState, id = id, modifier = modifier) {
        onEvent(it)
    }
}

@Composable
private fun HandleUiState(
    id: String?,
    uiState: PokemonDetailsUiState,
    modifier: Modifier,
    onEvent: (PokemonDetailsUiEvents) -> Unit,
) {
    val errorMessage = stringResource(R.string.http_response_generic_error_message)

    when (uiState) {
        is PokemonDetailsUiState.Initial -> {
            TrackScreen(screenName = SCREEN_NAME)
        }

        is PokemonDetailsUiState.Success -> {
            SuccessState(modifier = modifier, viewData = uiState.viewData)
        }

        is PokemonDetailsUiState.Error -> {
            ErrorUiState(
                errorData = uiState.error,
            ) {
                id?.let {
                    onEvent(
                        PokemonDetailsUiEvents.OnRetryButtonClicked(
                            id = id, errorMessage = errorMessage
                        )
                    )
                }
            }
        }

        is PokemonDetailsUiState.Loading -> {
            LoaderUiState()
        }
    }
}

@Composable
fun SnackbarUiState(
    viewModel: PokemonViewModelDetails, onSnackbarEvent: (String) -> Unit
) {
    val snackbarState by viewModel.snackbarState.collectAsStateWithLifecycle()

    if (snackbarState != null) {
        LaunchedEffect(snackbarState) {
            onSnackbarEvent((snackbarState as SnackbarUiStateHolder.SnackbarUi).msg)
        }
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier, viewData: PokemonDetailsViewData
) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
        border = BorderStroke(
            dimensionResource(R.dimen.card_border_elevation), Color.LightGray
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = dimensionResource(R.dimen.card_padding))
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.double_padding))
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.double_padding)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(viewData.imgUrl)
                            .crossfade(true).build(),
                        placeholder = painterResource(R.drawable.pokeball_icon),
                        error = painterResource(R.drawable.error_img),
                        contentDescription = "Image of ${viewData.name}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.double_padding)))
                    )
                    Column {
                        Text(
                            viewData.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "ID: ${viewData.id}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // Display types as chips (if available)
                        if (viewData.types.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.base_padding)),
                                modifier = Modifier.padding(top = dimensionResource(R.dimen.base_padding))
                            ) {
                                viewData.types.forEach { type ->
                                    TypeChip(type)
                                }
                            }
                        }
                    }
                }
            }
            if (viewData.stats.isNotEmpty()) {
                item {
                    SectionTitle("Base Stats")
                    StatsList(viewData.stats)
                }
            }
            item {
                SectionTitle(stringResource(R.string.measurement_title))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MeasurementItem(
                        stringResource(R.string.pokemon_weight_label), "${viewData.weight} kg"
                    )
                    MeasurementItem(
                        stringResource(R.string.pokemon_weight_label), "${viewData.height} m"
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.double_padding),
            bottom = dimensionResource(R.dimen.base_padding)
        )
    )
}

@Composable
fun StatsList(stats: List<Pair<String, String>>) {
    Column {
        stats.forEach { (name, value) ->
            StatItem(name, value)
        }
    }
}

@Composable
fun StatItem(name: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.small_padding)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MeasurementItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TypeChip(type: String) {
    Surface(
        shape = RoundedCornerShape(dimensionResource(R.dimen.base_padding)),
        color = getTypeColor(type),
        modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding))
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.base_padding),
                vertical = dimensionResource(R.dimen.small_padding)
            )
        )
    }
}

fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "fire" -> Color(0xFFFFA07A) // Light Salmon
        "water" -> Color(0xFFADD8E6) // Light Blue
        "grass" -> Color(0xFF90EE90) // Light Green
        "poison" -> Color(0xFFC183C1) // Purple
        "electric" -> Color(0xFFF0E68C) // Khaki
        "ground" -> Color(0xFFE6E6FA) // Lavender
        else -> Color.Gray // Default color for other types
    }
}

@Composable
@Preview
fun DetailsScreenPreview() {

}