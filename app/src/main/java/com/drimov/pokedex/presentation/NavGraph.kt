package com.drimov.pokedex.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.drimov.pokedex.presentation.pokedex_list.PokedexListScreen
import com.drimov.pokedex.presentation.pokedex_pokemon.PokedexPokemonScreen
import com.drimov.pokedex.presentation.pokedex_splash.PokedexSplashScreen
import com.drimov.pokedex.util.Routes

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.POKEDEX_SPLASH
    ) {
        composable(route = Routes.POKEDEX_SPLASH) {
            PokedexSplashScreen(onNavigate = { navController.navigate(it.route) })
        }
        composable(route = Routes.POKEDEX_LIST) {
            PokedexListScreen(onNavigate = { navController.navigate(it.route) })
        }
        composable(
            route = Routes.POKEDEX_POKEMON + "?id={id}?language={language}",
            arguments = listOf(

                navArgument(name = "id") {
                    type = NavType.IntType
                },
                navArgument(name = "language") {
                    type = NavType.StringType
                }
            )
        ) {
            PokedexPokemonScreen(onPopBackStack = { navController.popBackStack() })
        }
    }
}