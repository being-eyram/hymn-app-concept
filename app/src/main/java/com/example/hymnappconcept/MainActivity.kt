package com.example.hymnappconcept

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.hymnappconcept.ui.theme.HymnAppConceptTheme
import com.example.hymnappconcept.viewmodels.HymnContentViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {


    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight

            val navController = rememberNavController()
            val repository = (application as HymnApplication).repository

            HymnAppConceptTheme {
                Surface(color = MaterialTheme.colors.background) {

                    NavHost(navController = navController, startDestination = "ListScreen") {
                        composable("ListScreen") {
                            HymnsListScreen(repository, navController)
                        }
                        composable(
                            "ContentScreen/{clickedHymn}",
                            arguments = listOf(
                                navArgument("clickedHymn") { type = NavType.IntType })
                        ) { backStackEntry ->
                            HymnContentScreen(
                                navController = navController,
                                backStackEntry.arguments?.getInt("clickedHymn")!!,
                                hymnContentViewModel = HymnContentViewModel(repository)
                            )
                        }
                    }
                }
            }

            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                // dark icons if we're in light theme
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons
                )
            }
        }
    }
}


