package com.thiago.marvelcompose.view

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.thiago.marvelcompose.Destination
import com.thiago.marvelcompose.R
import com.thiago.marvelcompose.ui.theme.Red
import com.thiago.marvelcompose.viewmodel.LibraryApiViewModel

@Composable
fun CharacterBottonNav(navController: NavHostController, lvm: LibraryApiViewModel) {
    BottomNavigation(
        elevation = 5.dp,
        backgroundColor = Red
    ) {

        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination
        val iconLibrary = painterResource(id = R.drawable.ic_library)
        val iconColletion = painterResource(id = R.drawable.ic_collections)

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Library.route,
            onClick = { navController.navigate(Destination.Library.route){
                popUpTo(Destination.Library.route)
                launchSingleTop = true
            } },
            icon = {
                Icon(painter = iconLibrary , contentDescription = null )
            },
            label = {
                Text(
                    text = Destination.Library.route
                )
            }
        )

        BottomNavigationItem(
            selected = currentDestination?.route == Destination.Collection.route,
            onClick = { navController.navigate(Destination.Collection.route){
                popUpTo(Destination.Collection.route)
                launchSingleTop = true
            } },
            icon = {
                Icon(painter = iconColletion , contentDescription = null )
            },
            label = {
                Text(
                    text = Destination.Collection.route
                )
            }
        )
    }
}