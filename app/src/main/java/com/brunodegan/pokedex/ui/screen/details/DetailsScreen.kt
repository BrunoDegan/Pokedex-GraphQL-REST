package com.brunodegan.pokedex.ui.screen.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.ui.ErrorUiState
import com.brunodegan.pokedex.base.ui.LoaderUiState
import com.brunodegan.pokedex.base.ui.ObserveAsEvent
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.data.metrics.TrackScreen
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import com.brunodegan.pokedex.ui.screen.details.events.PokemonDetailsUiEvents
import com.brunodegan.pokedex.ui.screen.details.state.PokemonDetailsUiState
import com.brunodegan.pokedex.ui.screen.details.viewModel.PokemonViewModelDetails
import org.koin.androidx.compose.koinViewModel

private const val SCREEN_NAME = "Details"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    id: Int? = null,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: PokemonViewModelDetails = koinViewModel()
) {
    val errorMessage = stringResource(R.string.http_response_generic_error_message)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateUp()
    }

    LaunchedEffect(id) {
        viewModel.getPokemonDetails(id = id, errorMessage = errorMessage)
    }

    ObserveAsEvent(events = viewModel.snackbarState) { event ->
        when (event) {
            is SnackbarUiStateHolder.SnackbarUi -> {
                onShowSnackbar(event.msg)
            }
        }
    }

    DetailsScreen(
        id = id, scrollBehavior = scrollBehavior, onEvent = { event ->
            viewModel.onUiEvent(event)
        }, uiState = uiState, modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(
    id: Int?,
    scrollBehavior: TopAppBarScrollBehavior,
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
            SuccessState(
                scrollBehavior = scrollBehavior, modifier = modifier, viewData = uiState.viewData
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    viewData: PokemonDetailsViewData
) {

    val imageRequest = ImageRequest.Builder(LocalContext.current).data(viewData.imgUrl)
        .decoderFactory(SvgDecoder.Factory()).scale(Scale.FIT).crossfade(true)
        .placeholder(R.drawable.pokeball_icon).error(R.drawable.error_img).build()

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
                .padding(dimensionResource(R.dimen.base_padding))
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = "Image of ${viewData.name}",
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.details_image_size))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.double_padding)))
                    )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(dimensionResource(R.dimen.double_padding))
                    ) {
                        Text(
                            text = viewData.name.capitalize(Locale.current),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
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
                    SectionTitle(
                        stringResource(R.string.stats_title),
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.double_padding),
                        )
                    )
                    StatsList(
                        viewData.stats,
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.base_padding),
                            bottom = dimensionResource(R.dimen.double_padding),
                        )
                    )
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
                        stringResource(R.string.pokemon_height_label), "${viewData.height} cm"
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
fun StatsList(stats: List<Pair<String, String>>, modifier: Modifier) {
    Column(modifier = modifier) {
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
            .padding(
                top = dimensionResource(R.dimen.base_padding),
                bottom = dimensionResource(R.dimen.base_padding)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            name.capitalize(Locale.current),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                end = dimensionResource(R.dimen.small_padding)
            )
        )
        Box(
            modifier = Modifier
                .height(dimensionResource(R.dimen.stats_box_height))
                .fillMaxWidth(value.toFloat() / 100)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.tiny_padding)))
                .background(MaterialTheme.colorScheme.primary)
                .padding(
                    start = dimensionResource(R.dimen.small_padding),
                    end = dimensionResource(R.dimen.small_padding)
                )
        )
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
@OptIn(ExperimentalMaterial3Api::class)
fun DetailsScreenPreview() {
    DetailsScreen(
        id = null,
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        modifier = Modifier,
        onShowSnackbar = {},
        onNavigateUp = {},
        viewModel = koinViewModel()
    )
}