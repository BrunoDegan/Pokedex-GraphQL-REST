package com.brunodegan.pokedex.ui.screen.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.TrackScreen
import com.brunodegan.pokedex.base.routes.ScreenRoutes
import com.brunodegan.pokedex.base.ui.ErrorUiState
import com.brunodegan.pokedex.base.ui.LoaderUiState
import com.brunodegan.pokedex.base.ui.SnackbarUiStateHolder
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TrackScreen(screenName = ScreenRoutes.DETAILS.name)

    BackHandler {
        onNavigateUp()
    }

    val errorMessage = stringResource(R.string.http_response_generic_error_message)
    DetailsScreen(
        modifier = modifier,
        onShowSnackbar = onShowSnackbar,
        onTryAgainButtonClicked = {
            viewModel.getPokemonDetails(id = id, errorMessage = errorMessage)
        },
        viewModel = viewModel,
        uiState = uiState
    )
}

@Composable
private fun DetailsScreen(
    modifier: Modifier = Modifier,
    onTryAgainButtonClicked: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: PokemonViewModelDetails,
    uiState: PokemonDetailsUiState
) {
    SnackbarUiState(
        onSnackbarEvent = onShowSnackbar, viewModel = viewModel
    )
    HandleUiState(uiState, modifier, onTryAgainButtonClicked)
}

@Composable
private fun HandleUiState(
    uiState: PokemonDetailsUiState,
    modifier: Modifier,
    onTryAgainButtonClicked: () -> Unit
) {
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
                onTryAgainButtonClicked.invoke()
            }
        }

        is PokemonDetailsUiState.Loading -> {
            LoaderUiState()
        }

        else -> return
    }
}

@Composable
fun SnackbarUiState(
    viewModel: PokemonViewModelDetails,
    onSnackbarEvent: (String) -> Unit
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
    modifier: Modifier = Modifier,
    viewData: PokemonDetailsViewData
) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
        border = BorderStroke(dimensionResource(R.dimen.card_border_elevation), Color.LightGray),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = dimensionResource(R.dimen.card_padding))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding))
        ) {
            Text(
                text = viewData.name, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = viewData.height, style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = viewData.weight, style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
@Preview
fun DetailsScreenPreview() {

}