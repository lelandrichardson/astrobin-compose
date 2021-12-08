package com.example.astrobin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.LocalImageLoader
import com.example.astrobin.api.AstrobinApi
import com.example.astrobin.api.LocalAstrobinApi
import com.example.astrobin.ui.Routes
import com.example.astrobin.ui.screens.ImageScreen
import com.example.astrobin.ui.screens.SearchScreen
import com.example.astrobin.ui.screens.TopScreen
import com.example.astrobin.ui.screens.UserScreen
import com.example.astrobin.ui.theme.AstrobinTheme
import com.example.astrobin.ui.theme.DarkBlue
import com.example.astrobin.ui.theme.Yellow
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding

@Composable fun Astrobin(api: AstrobinApi, imageLoader: ImageLoader) {
  CompositionLocalProvider(
    LocalAstrobinApi provides api,
    LocalImageLoader provides imageLoader
  ) {
    val nav = rememberNavController()
    AstrobinTheme {
      AstroAppWindow(
        Modifier.fillMaxSize().navigationBarsPadding(),
        top = {},
        bottom = {
          AstrobinBottomNav(nav)
        },
      ) { padding ->
        NavHost(nav, startDestination = "home") {
          composable("home") {
            UserScreen(93620, padding, nav)
          }
          composable(
            "user/{id}",
            listOf(navArgument("id") { type = NavType.IntType })
          ) {
            UserScreen(it.arguments?.getInt("id")!!, padding, nav)
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
            TopScreen(padding, nav)
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
    },
    selectedContentColor = Yellow,
    unselectedContentColor = Color.White,
  )
}

@Composable fun AstrobinBottomNav(nav: NavController) {
  BottomNavigation(
    Modifier.clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp)),
    backgroundColor = Color.Black,
  ) {
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


private val mainWindowGradient = Brush.linearGradient(
  listOf(Color.Black, DarkBlue),
  start = Offset.Zero,
  end = Offset(0f, Float.POSITIVE_INFINITY)
)
private enum class AstroScaffoldLayoutContent { TopBar, MainContent, BottomBar }
@Composable fun AstroAppWindow(
  modifier: Modifier = Modifier,
  top: @Composable () -> Unit,
  bottom: @Composable () -> Unit,
  content: @Composable (PaddingValues) -> Unit
) {
  ProvideWindowInsets {
    CompositionLocalProvider(
      LocalContentColor provides Color.White,
    ) {
      SubcomposeLayout(
        modifier.background(mainWindowGradient)
      ) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
          val topBarPlaceables = subcompose(AstroScaffoldLayoutContent.TopBar, top)
            .map { it.measure(looseConstraints) }

          val topBarHeight = topBarPlaceables.maxByOrNull { it.height }?.height ?: 0

          val bottomBarPlaceables = subcompose(AstroScaffoldLayoutContent.BottomBar, bottom)
            .map { it.measure(looseConstraints) }

          val bottomBarHeight = bottomBarPlaceables.maxByOrNull { it.height }?.height ?: 0

          val bodyContentHeight = layoutHeight - topBarHeight

          val bodyContentPlaceables = subcompose(AstroScaffoldLayoutContent.MainContent) {
            val innerPadding = PaddingValues(bottom = bottomBarHeight.toDp())
            content(innerPadding)
          }.map { it.measure(looseConstraints.copy(maxHeight = bodyContentHeight)) }

          // Placing to control drawing order to match default elevation of each placeable

          bodyContentPlaceables.forEach {
            it.place(0, topBarHeight)
          }
          topBarPlaceables.forEach {
            it.place(0, 0)
          }
          // The bottom bar is always at the bottom of the layout
          bottomBarPlaceables.forEach {
            it.place(0, layoutHeight - bottomBarHeight)
          }
        }
      }
    }
  }
}