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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.TrackScreen
import com.brunodegan.pokedex.base.routes.ScreenRoutes
import com.brunodegan.pokedex.base.ui.ErrorUiState
import com.brunodegan.pokedex.base.ui.LoaderUiState
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
import com.brunodegan.pokedex.ui.models.PokemonListViewData
import com.brunodegan.pokedex.ui.screen.home.state.PokemonListUiState
import com.brunodegan.pokedex.ui.screen.home.viewModel.PokemonListViewModel
import org.koin.androidx.compose.koinViewModel

private const val SCREEN_NAME = "Home"

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onShowSnackbar: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onCardClicked: (String) -> Unit,
    viewModel: PokemonListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = stringResource(R.string.http_response_generic_error_message)
    TrackScreen(screenName = ScreenRoutes.HOME.name)

    BackHandler {
        onNavigateUp()
    }

    LaunchedEffect(Unit) {
        viewModel.getPokemons(errorMessage = errorMessage)
    }
    HomeScreen(
        onShowSnackbar = onShowSnackbar,
        uiState = uiState,
        viewModel = viewModel,
        onCardClicked = onCardClicked,
        onRetryButtonClicked = {
            viewModel.getPokemons(errorMessage = errorMessage)
        },
        modifier = modifier
    )
}


@Composable
internal fun HomeScreen(
    uiState: PokemonListUiState,
    onCardClicked: (String) -> Unit,
    onRetryButtonClicked: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: PokemonListViewModel,
    modifier: Modifier,
) {
    SnackbarUiState(
        onSnackbarEvent = onShowSnackbar, viewModel = viewModel
    )
    HandleUiState(
        uiState = uiState,
        onRetryButtonClicked = onRetryButtonClicked,
        onCardClicked = onCardClicked,
        modifier = modifier
    )
}

@Composable
private fun HandleUiState(
    uiState: PokemonListUiState,
    onCardClicked: (String) -> Unit,
    onRetryButtonClicked: () -> Unit,
    modifier: Modifier
) {
    when (uiState) {
        is PokemonListUiState.Initial -> {
            TrackScreen(screenName = SCREEN_NAME)
        }

        is PokemonListUiState.Success -> {
            SuccessState(
                modifier = modifier, viewData = uiState.viewData, onCardClicked = onCardClicked
            )
        }

        is PokemonListUiState.Error -> {
            ErrorUiState(
                modifier = modifier,
                errorData = uiState.error,
            ) {
                onRetryButtonClicked()
            }
        }

        is PokemonListUiState.Loading -> {
            LoaderUiState()
        }

        else -> {

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

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    onCardClicked: (String) -> Unit,
    viewData: List<PokemonListViewData>
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        modifier = modifier.fillMaxSize()
    ) {
        items(viewData.size, key = { index -> viewData[index].name }) { position ->
            PokemonCard(viewData = viewData[position], onCardClicked = onCardClicked)
        }
    }
}

@Composable
private fun PokemonCard(
    modifier: Modifier = Modifier, onCardClicked: (String) -> Unit, viewData: PokemonListViewData
) {
    val cardPadding = dimensionResource(R.dimen.card_padding)
    val LocalCardsPadding = compositionLocalOf { cardPadding }

    val imageRequest = ImageRequest.Builder(LocalContext.current).data(viewData.imgUrl)
        .decoderFactory(SvgDecoder.Factory()).scale(Scale.FIT)
        .placeholder(R.drawable.placeholder_foreground).error(R.drawable.error_img).build()

    CompositionLocalProvider(LocalCardsPadding provides cardPadding) {
        Card(
            colors = CardDefaults.elevatedCardColors().copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.secondaryContainer
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
                .clickable(enabled = true, onClick = {
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
                    horizontalAlignment = Alignment.Start,
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
                        contentDescription = "",
                        modifier = Modifier
                            .padding(all = dimensionResource(R.dimen.double_padding))
                            .size(dimensionResource(R.dimen.pokemon_card_avatar_size))
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
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.double_padding))
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    viewData.types.forEach {
                        Text(
                            text = it.capitalize(Locale.current),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(
                                dimensionResource(R.dimen.base_padding),
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = PokemonListUiState.Initial,
        onCardClicked = {},
        onRetryButtonClicked = {},
        onShowSnackbar = {},
        viewModel = TODO(),
        modifier = TODO(),
    )
}