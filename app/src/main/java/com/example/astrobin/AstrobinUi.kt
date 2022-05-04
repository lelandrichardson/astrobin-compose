package com.example.astrobin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.example.astrobin.api.ApiTest
import com.example.astrobin.api.Astrobin
import com.example.astrobin.ui.Routes
import com.example.astrobin.ui.screens.*
import com.example.astrobin.ui.theme.AstrobinTheme
import com.example.astrobin.ui.theme.DarkBlue
import com.example.astrobin.ui.theme.Yellow
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding

@Composable fun Astrobin(api: Astrobin, imageLoader: ImageLoader) {
  CompositionLocalProvider(
    LocalAstrobinApi provides api,
    LocalImageLoader provides imageLoader
  ) {
    val nav = rememberNavController()
    val current by nav.currentBackStackEntryAsState()
    AstrobinTheme {
      AstroAppWindow(
        Modifier
          .fillMaxSize()
          .navigationBarsPadding(),
        top = {
          if (current != null && nav.previousBackStackEntry != null) {
            IconButton(
              modifier = Modifier.statusBarsPadding().padding(8.dp),
              onClick = { nav.popBackStack() }
            ) {
              Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
          }
        },
        bottom = {
          if (current?.destination?.route?.startsWith("fullscreen") != true) {
            AstrobinBottomNav(nav)
          }
        },
      ) { padding ->
        NavHost(nav, startDestination = "home") {
          composable("home") {
//            UserScreen(93620, padding, nav)
//            ImageScreen("v7v9fq", padding, nav)
            ApiTest()
//            TopScreen(padding = padding, nav = nav)
//            SearchScreen(nav = nav, entry = it, padding = padding)
          }
          composable("profile") {
            // NOTE: hardcoded to leland's user id for demo purposes
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
            ImageScreen(it.arguments!!.getString("hash")!!, padding, nav)
          }
          composable(
            "search?q={q}",
            listOf(navArgument("q") { type = NavType.StringType })
          ) {
            SearchScreen(nav = nav, entry = it, padding)
          }
          composable(
            "fullscreen?hd={hd}&solution={solution}&w={w}&h={h}",
            listOf(
              navArgument("hd") { type = NavType.StringType },
              navArgument("solution") { type = NavType.StringType; nullable = true },
              navArgument("w") { type = NavType.IntType },
              navArgument("h") { type = NavType.IntType },
            )
          ) {
            FullScreen(
              it.arguments!!.getString("hd")!!,
              it.arguments?.getString("solution"),
              it.arguments!!.getInt("w"),
              it.arguments!!.getInt("h"),
              padding,
              nav
            )
          }
          composable(Routes.Top) {
            TopScreen(padding, nav)
          }
          composable(Routes.Search) {
            SearchScreen(nav, entry = it, padding)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable fun AstrobinBottomNav(nav: NavController) {
  Box {
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val current = navBackStackEntry?.destination
    BottomNavigation(
      Modifier
        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        .align(Alignment.BottomCenter),
      backgroundColor = Color.Black,
    ) {
      AstrobinBottomNavigationItem(
        route = Routes.Top,
        name = "TOP",
        icon = Icons.Filled.Star,
        nav = nav,
        current = current
      )
      Spacer(Modifier.width(48.dp))
      AstrobinBottomNavigationItem(
        route = Routes.Profile,
        name = "PROFILE",
        icon = Icons.Filled.Person,
        nav = nav,
        current = current
      )
    }
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter),
      elevation = 40.dp,
      shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
      color = DarkBlue,
      contentColor = Color.White,
      onClick = {
        nav.navigate(Routes.Search) {
          popUpTo(nav.graph.findStartDestination().id) {
            saveState = true
          }
          launchSingleTop = true
          restoreState = true
        }
      }
    ) {
      Icon(
        Icons.Filled.Search,
        contentDescription = null,
        tint = DarkBlue,
        modifier = Modifier
          .padding(
            start = 20.dp,
            end = 20.dp,
            top = 16.dp,
            bottom = 20.dp
          )
          .background(Yellow, CircleShape)
          .padding(8.dp)
          .size(30.dp)
      )
    }
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

          // hard code to 0 as we want the top bar to "float" on top of things
          val topBarHeight = 0 // topBarPlaceables.maxByOrNull { it.height }?.height ?: 0

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