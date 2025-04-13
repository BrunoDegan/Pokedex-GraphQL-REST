package com.brunodegan.pokedex.base.navigation

import HomeScreen
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.brunodegan.pokedex.R
import com.brunodegan.pokedex.base.routes.DetailsRoute
import com.brunodegan.pokedex.base.routes.HomeRoute
import com.brunodegan.pokedex.base.ui.BaseScreen
import com.brunodegan.pokedex.base.ui.PokemonAppBar
import com.brunodegan.pokedex.ui.screen.details.DetailsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalView.current.context as? Activity
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    NavHost(navController, startDestination = HomeRoute) {
        composable<HomeRoute> { navBackStackEntry ->
            navBackStackEntry.id
            BaseScreen(
                snackbarHostState = snackbarHostState,
                snackbar = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.onError)
                    )
                },
                topBar = {
                    PokemonAppBar(
                        scrollBehavior = scrollBehavior,
                        title = stringResource(R.string.app_name),
                        onBackButtonClicked = {
                            val popped = navController.popBackStack()
                            if (!popped) {
                                activity?.finish()
                            }
                        }, actions = {
                        }
                    )
                },
            ) { paddingValue ->
                HomeScreen(
                    onShowSnackbar = { msg ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onNavigateUp = {
                        val popped = navController.popBackStack()
                        if (!popped) {
                            activity?.finish()
                        }
                    },
                    onCardClicked = { id ->
                        navController.navigate(DetailsRoute(id = id))
                    },
                    modifier = Modifier.padding(paddingValue),
                )
            }
        }
        composable<DetailsRoute> { navBackStackEntry ->

            val detailsRouteArgs = navBackStackEntry.toRoute<DetailsRoute>()

            BaseScreen(
                snackbarHostState = snackbarHostState,
                snackbar = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.onError)
                    )
                },
                topBar = {
                    PokemonAppBar(
                        scrollBehavior = scrollBehavior,
                        title = stringResource(R.string.app_name),
                        onBackButtonClicked = {
                            navController.popBackStack()
                        }, actions = {

                        }
                    )
                },
            ) { paddingValue ->

                DetailsScreen(
                    onNavigateUp = {
                        navController.popBackStack()
                    },
                    onShowSnackbar = { msg ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = msg
                            )
                        }
                    },
                    id = detailsRouteArgs.id,
                    modifier = Modifier.padding(paddingValue),
                )
            }
        }
    }
}