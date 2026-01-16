// app/src/main/java/com/example/kulturgambia/ui/KulturGambiaApp.kt
package com.example.kulturgambia.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kulturgambia.R
import com.example.kulturgambia.data.AppGraph
import com.example.kulturgambia.ui.screens.CategoryScreen
import com.example.kulturgambia.ui.screens.DetailScreen
import com.example.kulturgambia.ui.screens.EventsScreen
import com.example.kulturgambia.ui.screens.EventDetailScreen
import com.example.kulturgambia.ui.screens.ExploreScreen
import com.example.kulturgambia.ui.screens.HomeScreen
import com.example.kulturgambia.ui.screens.MapScreen
import com.example.kulturgambia.ui.screens.SitesScreen
import com.example.kulturgambia.ui.screens.SiteDetailScreen
import com.example.kulturgambia.ui.screens.SubmitStoryScreen
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Place


sealed class BottomDest(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomDest("home", "Home", Icons.Filled.Home)
    data object Explore : BottomDest("explore", "Explore", Icons.Filled.Book)
    data object Map : BottomDest("map", "Map", Icons.Filled.Map)
    data object Events : BottomDest("events", "Events", Icons.Filled.Event)
    data object Sites : BottomDest("sites", "Sites", Icons.Filled.Place)
}


private const val SPLASH_ROUTE = "splash"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KulturGambiaApp() {
    val navController = rememberNavController()
    val graph = remember { AppGraph.current() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val topLevelRoutes = setOf(
        BottomDest.Home.route,
        BottomDest.Explore.route,
        BottomDest.Map.route,
        BottomDest.Events.route,
        BottomDest.Sites.route
    )

    val isSplash = currentRoute == SPLASH_ROUTE

    val canNavigateBack = navController.previousBackStackEntry != null &&
            (currentRoute !in topLevelRoutes) &&
            !isSplash

    var searchOpen by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val supportsSearch =
        currentRoute == BottomDest.Home.route || currentRoute == BottomDest.Explore.route

    val routeKey = currentRoute ?: ""
    remember(routeKey) {
        if (!supportsSearch) {
            searchOpen = false
            searchQuery = ""
        }
    }

    Scaffold(
        topBar = {
            if (!isSplash) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        if (searchOpen && supportsSearch) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Search culture…") },
                                singleLine = true,
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    imeAction = ImeAction.Search
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        } else {
                            Text(
                                when {
                                    currentRoute?.startsWith("detail/") == true -> "Details"
                                    currentRoute?.startsWith("category/") == true -> "Category"
                                    currentRoute == BottomDest.Home.route -> "Kultur Gambia"
                                    currentRoute == BottomDest.Explore.route -> "Explore"
                                    currentRoute == BottomDest.Map.route -> "Map"
                                    currentRoute == BottomDest.Events.route -> "Events"
                                    currentRoute == BottomDest.Sites.route -> "Sites to Visit"
                                    else -> "Kultur Gambia"
                                }
                            )
                        }
                    },
                    navigationIcon = {
                        when {
                            searchOpen && supportsSearch -> {
                                IconButton(onClick = {
                                    searchOpen = false
                                    searchQuery = ""
                                }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Close search")
                                }
                            }

                            canNavigateBack -> {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        if (supportsSearch) {
                            if (!searchOpen) {
                                IconButton(onClick = { searchOpen = true }) {
                                    Icon(Icons.Filled.Search, contentDescription = "Search")
                                }
                            } else if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (!isSplash) BottomBar(navController)
        }
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = SPLASH_ROUTE,
            modifier = Modifier.padding(inner)
        ) {
            composable(SPLASH_ROUTE) {
                KulturSplashScreen(
                    onDone = {
                        navController.navigate(BottomDest.Home.route) {
                            popUpTo(SPLASH_ROUTE) { inclusive = true }
                        }
                    }
                )
            }

            composable(BottomDest.Home.route) {
                HomeScreen(
                    items = graph.cultureRepository.getAll(),
                    searchQuery = searchQuery,
                    onItemClick = { item -> navController.navigate("detail/${item.id}") },
                    onSubmitClick = { navController.navigate("submit") }
                )
            }

            composable(BottomDest.Explore.route) {
                ExploreScreen(
                    repo = graph.cultureRepository,
                    onOpenItem = { id -> navController.navigate("detail/$id") }
                )
            }

            composable("category/{category}") { backStack ->
                val category = backStack.arguments?.getString("category").orEmpty()
                CategoryScreen(
                    category = category,
                    repo = graph.cultureRepository,
                    onOpenItem = { id -> navController.navigate("detail/$id") }
                )
            }

            composable("submit") {
                SubmitStoryScreen(
                    onPost = { title, category, summary, content, imageUri ->
                        val success = graph.cultureRepository.addPost(
                            title = title,
                            category = category,
                            summary = summary,
                            content = content,
                            coverImageDrawable = imageUri.toString()
                        )

                        if (success) {
                            navController.popBackStack()
                        }

                        success
                    }
                )
            }


            composable(BottomDest.Map.route) { MapScreen(repo = graph.placesRepository) }

            composable(BottomDest.Events.route) {
                EventsScreen(
                    eventRepo = graph.eventsRepository,
                    placeRepo = graph.placesRepository,
                    onOpenEvent = { id -> navController.navigate("event/$id") }
                )
            }


            composable("event/{id}") { backStack ->
                val id = backStack.arguments?.getString("id").orEmpty()

                EventDetailScreen(
                    eventId = id,
                    eventsRepo = graph.eventsRepository,
                    placesRepo = graph.placesRepository,
                    onBack = { navController.popBackStack() },
                    onOpenMap = { lat: Double, lng: Double, name: String ->
                        navController.navigate("map_preview")
                    }


                )
            }

            composable("map_preview") {
                MapScreen(repo = graph.placesRepository)
            }


            composable("map_preview/{lat}/{lng}/{name}") { backStack ->
                val lat = backStack.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
                val lng = backStack.arguments?.getString("lng")?.toDoubleOrNull() ?: 0.0
                val name = backStack.arguments?.getString("name").orEmpty()

                // For now, just show your existing MapScreen
                // Later we can use lat/lng/name to auto-focus the map if your MapScreen supports it
                MapScreen(repo = graph.placesRepository)
            }



            composable(BottomDest.Sites.route) {
                SitesScreen(
                    placesRepo = graph.placesRepository,
                    onOpenPlace = { id -> navController.navigate("place/$id") }
                )
            }

            composable("place/{id}") { backStack ->
                val id = backStack.arguments?.getString("id").orEmpty()

                SiteDetailScreen(
                    placeId = id,
                    placesRepo = graph.placesRepository,
                    onBack = { navController.popBackStack() },
                    onOpenMap = { lat: Double, lng: Double, name: String ->
                        val safeName = java.net.URLEncoder.encode(name, "UTF-8")
                        navController.navigate("map_preview/$lat/$lng/$safeName")
                    }
                )
            }



            composable("detail/{id}") { backStack ->
                val id = backStack.arguments?.getString("id").orEmpty()
                DetailScreen(
                    id = id,
                    cultureRepo = graph.cultureRepository,
                    favoritesRepo = graph.favoritesRepository,
                    onBack = { navController.popBackStack() },
                    onEdit = { /* TODO */ },
                    onDeleteSuccess = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun KulturSplashScreen(onDone: () -> Unit) {
    // small delay so splash feels intentional
    LaunchedEffect(Unit) {
        delay(900)
        onDone()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // put your generated map image here
            Image(
                painter = painterResource(id = R.drawable.flag),
                contentDescription = "Kultur Gambia",
                modifier = Modifier.size(140.dp)
            )
        }

        Text(
            text = "Kultur Gambia",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomDest.Home,
        BottomDest.Explore,
        BottomDest.Map,
        BottomDest.Events,
        BottomDest.Sites
    )
    val current = currentRoute(navController)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        items.forEach { dest ->
            val selected = current == dest.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(dest.route) {
                        popUpTo(BottomDest.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.label
                    )
                },
                label = { Text(dest.label) },
                alwaysShowLabel = true
                // no colors block (older Material3 compatibility)
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val backStackEntry by navController.currentBackStackEntryAsState()
    return backStackEntry?.destination?.route
}
