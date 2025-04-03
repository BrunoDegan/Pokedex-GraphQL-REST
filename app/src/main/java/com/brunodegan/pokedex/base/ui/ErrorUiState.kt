package com.brunodegan.pokedex.base.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.content.res.ResourcesCompat
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.errors.ErrorData

@Composable
fun ErrorUiState(
    modifier: Modifier = Modifier, errorData: ErrorData, onRetryButtonClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(
                    alpha = ResourcesCompat.getFloat(
                        LocalContext.current.resources, R.dimen.progress_circular_indicator_alpha
                    )
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(R.dimen.double_padding),
                    end = dimensionResource(R.dimen.double_padding)
                )
        ) {
            Text(
                text = errorData.errorMsg,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Visible,
                maxLines = integerResource(R.integer.card_lines),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(R.dimen.base_padding),
                        bottom = dimensionResource(R.dimen.base_padding)
                    )
            )
            Button(
                contentPadding = PaddingValues(dimensionResource(R.dimen.base_padding)),
                elevation = ButtonDefaults.elevatedButtonElevation(),
                shape = ButtonDefaults.elevatedShape,
                border = BorderStroke(
                    dimensionResource(R.dimen.tiny_padding), color = colorResource(R.color.teal_700)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = dimensionResource(R.dimen.double_padding),
                        start = dimensionResource(R.dimen.double_padding),
                    ),
                onClick = {
                    onRetryButtonClicked()
                }) {
                Text(
                    text = stringResource(R.string.try_again_label),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(dimensionResource(R.dimen.card_padding))

                )
            }
        }
    }
}