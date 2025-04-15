import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.ui.ErrorUiState
import com.brunodegan.pokedex.base.ui.LoaderUiState
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.data.metrics.TrackScreen
import com.brunodegan.pokedex.ui.models.PokemonListViewData
import com.brunodegan.pokedex.ui.screen.home.PokemonListUiEvents
import com.brunodegan.pokedex.ui.screen.home.state.PokemonListUiState
import com.brunodegan.pokedex.ui.screen.home.viewModel.PokemonListViewModel
import org.koin.androidx.compose.koinViewModel

private const val SCREEN_NAME = "Home"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    onShowSnackbar: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onCardClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.http_response_generic_error_message)

    BackHandler {
        onNavigateUp()
    }

    LaunchedEffect(Unit) {
        viewModel.getPokemons(errorMessage = errorMessage)
    }
    HomeScreen(
        scrollBehavior = scrollBehavior,
        onShowSnackbar = onShowSnackbar,
        state = uiState,
        viewModel = viewModel,
        onEvent = {
            viewModel.onEvent(
                event = it,
                onCardClicked = onCardClicked
            )
        },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    state: PokemonListUiState,
    onEvent: (PokemonListUiEvents) -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: PokemonListViewModel,
    modifier: Modifier,
) {
    SnackbarUiState(
        onSnackbarEvent = onShowSnackbar, viewModel = viewModel
    )

    HandleUiState(
        scrollBehavior = scrollBehavior,
        state = state,
        onEvent = onEvent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleUiState(
    scrollBehavior: TopAppBarScrollBehavior,
    state: PokemonListUiState,
    onEvent: (PokemonListUiEvents) -> Unit,
    modifier: Modifier
) {
    val errorMessage = stringResource(R.string.http_response_generic_error_message)

    when (state) {
        is PokemonListUiState.Initial -> {
            TrackScreen(screenName = SCREEN_NAME)
        }

        is PokemonListUiState.Success -> {
            SuccessState(
                scrollBehavior = scrollBehavior,
                modifier = modifier,
                viewData = state.viewData
            ) {
                onEvent(PokemonListUiEvents.OnPokemonClickedUiEvent(it))
            }
        }

        is PokemonListUiState.Error -> {
            ErrorUiState(
                modifier = modifier,
                errorData = state.error,
            ) {
                onEvent(PokemonListUiEvents.OnRetryButtonClickedUiEvent(errorMessage = errorMessage))
            }
        }

        is PokemonListUiState.Loading -> {
            LoaderUiState()
        }
    }
}

@Composable
fun SnackbarUiState(
    viewModel: PokemonListViewModel, onSnackbarEvent: (String) -> Unit
) {
    val snackbarState by viewModel.snackbarState.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarState) {
        if (snackbarState != null) {
            onSnackbarEvent((snackbarState as SnackbarUiStateHolder.SnackbarUi).msg)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessState(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    viewData: List<PokemonListViewData>,
    onCardClicked: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        items(viewData.size, key = { index -> viewData[index].id }) { position ->
            PokemonCard(viewData = viewData[position], onCardClicked = onCardClicked)
        }
    }
}

@Composable
private fun PokemonCard(
    modifier: Modifier = Modifier, onCardClicked: (Int) -> Unit, viewData: PokemonListViewData
) {
    val cardPadding = dimensionResource(R.dimen.card_padding)
    val LocalCardsPadding = compositionLocalOf { cardPadding }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(viewData.imgUrl)
        .decoderFactory(SvgDecoder.Factory()).scale(Scale.FIT)
        .crossfade(true)
        .placeholder(R.drawable.pokeball_icon)
        .error(R.drawable.error_img)
        .build()

    CompositionLocalProvider(LocalCardsPadding provides cardPadding) {
        Card(
            colors = CardDefaults.elevatedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
            border = BorderStroke(
                dimensionResource(R.dimen.card_border_elevation), MaterialTheme.colorScheme.tertiary
            ),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(all = LocalCardsPadding.current)
                .clickable(
                    onClick = {
                        onCardClicked.invoke(viewData.id)
                    })
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(LocalCardsPadding.current)
                ) {
                    Text(
                        text = "#${viewData.id}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(
                                dimensionResource(R.dimen.base_padding),
                            )
                            .align(AbsoluteAlignment.Left)
                    )
                    AsyncImage(
                        model = imageRequest,
                        error = painterResource(R.drawable.error_img),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(all = dimensionResource(R.dimen.double_padding))
                            .size(dimensionResource(R.dimen.pokemon_card_avatar_size))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.double_padding)))
                    )
                    Text(
                        fontStyle = FontStyle.Italic,
                        text = viewData.name.capitalize(Locale.current),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(
                            dimensionResource(R.dimen.base_padding),
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)),
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.double_padding))
                        .wrapContentSize()
                ) {

                    Text(
                        text = viewData.types.joinToString {
                            it.capitalize(Locale.current)
                        },
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.W600,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(
                            dimensionResource(R.dimen.small_padding),
                        )
                    )
                    Text(
                        text = stringResource(R.string.pokemon_abilities_label),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = TextUnit(
                            value = ResourcesCompat.getFloat(
                                LocalContext.current.resources, R.dimen.abilities_title_font_size
                            ), type = TextUnitType.Sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.double_padding)
                        )
                    )
                    Text(
                        text = viewData.abilities.joinToString().capitalize(Locale.current),
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.W800,
                        fontSize = TextUnit(
                            value = ResourcesCompat.getFloat(
                                LocalContext.current.resources, R.dimen.abilities_font_size
                            ), type = TextUnitType.Sp
                        ),
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.padding(
                            top = dimensionResource(R.dimen.small_padding)
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        state = PokemonListUiState.Initial,
        onEvent = {},
        onShowSnackbar = {},
        viewModel = TODO(),
        modifier = TODO(),
    )
}