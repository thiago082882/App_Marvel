package com.thiago.marvelcompose

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thiago.marvelcompose.ui.theme.MarvelComposeTheme
import com.thiago.marvelcompose.view.CharacterBottonNav
import com.thiago.marvelcompose.view.CharacterDetailScreen
import com.thiago.marvelcompose.view.CollectionScreen
import com.thiago.marvelcompose.view.LibraryScreen
import com.thiago.marvelcompose.viewmodel.CollectionDbViewModel
import com.thiago.marvelcompose.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    object Library : Destination("library")
    object Collection : Destination("collection")
    object CharacterDetail : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }

}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private  val lvm by viewModels<LibraryApiViewModel>()
    private  val cvm by viewModels<CollectionDbViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharacterScaffold(navController = navController,lvm, cvm)
                }
            }
        }
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CharacterScaffold(navController: NavHostController,lvm : LibraryApiViewModel,cvm : CollectionDbViewModel) {

    var scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current

    androidx.compose.material.Scaffold(
        scaffoldState =scaffoldState,
        bottomBar = {
            CharacterBottonNav(navController = navController,lvm)
        }
    ) { paddingValue ->

        NavHost(
            navController = navController ,
            startDestination = Destination.Library.route
        ){

            composable(Destination.Library.route){
                LibraryScreen(navController,lvm,paddingValue)
            }

            composable(Destination.Collection.route){
                CollectionScreen(cvm,navController)
            }

            composable(Destination.CharacterDetail.route){ navBackStackEntry->
               val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                if(id==null)
                    Toast.makeText(ctx, "Character id is required", Toast.LENGTH_SHORT).show()
                else
                    lvm.retrieveSingleCharacter(id)
                CharacterDetailScreen(
                    lvm = lvm,
                    cvm = cvm,
                    paddingValues = paddingValue ,
                    navController = navController)
            }
        }

    }
}
