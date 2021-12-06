package com.example.astrobin

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.astrobin.api.AstrobinApi
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.ui.Routes
import com.example.astrobin.ui.screens.ImageScreen
import com.example.astrobin.ui.screens.SearchScreen
import com.example.astrobin.ui.screens.TopScreen
import com.example.astrobin.ui.screens.UserScreen
import com.example.astrobin.ui.theme.AstrobinTheme

@Composable fun Astrobin(api: AstrobinApi) {
  CompositionLocalProvider(LocalAstrobinApi provides api) {
    AstrobinTheme {
      val nav = rememberNavController()
      Scaffold(
        bottomBar = { AstrobinBottomNav(nav) }
      ) { innerPadding ->
        NavHost(nav, startDestination = "home", Modifier.padding(innerPadding)) {
          composable("home") {
            ImageScreen("nlw5b0", nav)
          }
          composable(
            "user/{id}",
            listOf(navArgument("id") { type = NavType.IntType })
          ) {
            UserScreen(it.arguments?.getInt("id")!!, nav)
          }
          composable(
            "image/{hash}",
            listOf(navArgument("hash") { type = NavType.StringType })
          ) {
            ImageScreen(it.arguments!!.getString("hash")!!, nav)
          }
          composable(Routes.Search) {
            SearchScreen(nav = nav, entry = it)
          }
          composable(Routes.Top) {
            TopScreen(nav)
          }
        }
      }
    }
  }
}

@Composable fun RowScope.AstrobinBottomNavigationItem(
  route: String,
  name: String,
  icon: ImageVector,
  nav: NavController,
  current: NavDestination?,
) {
  BottomNavigationItem(
    icon = { Icon(icon, contentDescription = null) },
    label = { Text(name) },
    selected = current?.hierarchy?.any { it.route == route } == true,
    onClick = {
      nav.navigate(route) {
        popUpTo(nav.graph.findStartDestination().id) {
          saveState = true
        }
        launchSingleTop = true
        restoreState = true
      }
    }
  )
}

@Composable fun AstrobinBottomNav(nav: NavController) {
  BottomNavigation {
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val current = navBackStackEntry?.destination
    AstrobinBottomNavigationItem(
      route = Routes.Top,
      name = "Top",
      icon = Icons.Filled.Star,
      nav = nav,
      current = current
    )
    AstrobinBottomNavigationItem(
      route = Routes.Latest,
      name = "Latest",
      icon = Icons.Filled.DateRange,
      nav = nav,
      current = current
    )
    AstrobinBottomNavigationItem(
      route = Routes.Search,
      name = "Search",
      icon = Icons.Filled.Search,
      nav = nav,
      current = current
    )
  }
}