package com.brunodegan.pokedex.base.navigation

import HomeScreen
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.brunodegan.pokedex.base.routes.DetailsRoute
import com.brunodegan.pokedex.base.routes.HomeRoute
import com.brunodegan.pokedex.base.ui.BaseScreen
import com.brunodegan.pokedex.ui.screen.details.DetailsScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    NavHost(navController, startDestination = HomeRoute) {
        composable<HomeRoute> { navBackStackEntry ->
            navBackStackEntry.id
            BaseScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                snackbarHostState = snackbarHostState,
                snackbar = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                topBar = {

                },
            ) {
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
                        navController.popBackStack()
                    },
                    onCardClicked = { id ->
                        navController.navigate(DetailsRoute(id = id))
                    }
                )
            }
        }
        composable<DetailsRoute> { navBackStackEntry ->

            val detailsRouteArgs = navBackStackEntry.toRoute<DetailsRoute>()

            BaseScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                snackbarHostState = snackbarHostState,
                snackbar = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                topBar = {

                },
            ) {
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
                    modifier = Modifier,
                )
            }
        }
    }
}