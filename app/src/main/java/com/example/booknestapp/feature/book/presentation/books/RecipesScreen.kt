package com.example.booknestapp.feature.book.presentation.books

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Snackbar
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.booknestapp.feature.book.domain.model.Recipe
import com.example.booknestapp.feature.book.presentation.books.components.RecipeItem
import com.example.booknestapp.feature.book.presentation.util.Screen
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    navController: NavController,
    viewModel: RecipesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val showDialog = remember { mutableStateOf(false) }
    val recipeToDelete = remember { mutableStateOf<Recipe?>(null) }
    val hasFetchedRecipes = viewModel.hasFetchedRecipes.value
    val showSnackbar = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    fun checkInternetAndUpdateSnackbar() {
        val activeNetwork = connectivityManager?.activeNetwork
        val networkCapabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
        val hasInternet = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

        if (!hasInternet) {
            if (!hasFetchedRecipes) {
                showSnackbar.value = true
            }
            else {
                showSnackbar.value = false
            }
        } else {
            showSnackbar.value = false
        }
        Log.d("Clicked","I clicked got flag show $showSnackbar")
        Log.d("Clicked","I clicked got flag fetched $hasFetchedRecipes")
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            checkInternetAndUpdateSnackbar()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            checkInternetAndUpdateSnackbar()
        }
    }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    LaunchedEffect(Unit) {
        connectivityManager?.requestNetwork(networkRequest, networkCallback)
        checkInternetAndUpdateSnackbar()
    }

    // Add this LaunchedEffect to trigger the check when hasFetchedRecipes changes
    LaunchedEffect(hasFetchedRecipes) {
        checkInternetAndUpdateSnackbar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Recepies") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditRecipeScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Recipe")
            }
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.recipes) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.AddEditRecipeScreen.route + "?recipeId=${recipe.id}")
                            },
//                        onDeleteClick = {
//                            recipeToDelete.value = recipe
//                            showDialog.value = true
//                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

//    if (showDialog.value) {
//        AlertDialog(
//            onDismissRequest = { showDialog.value = false },
//            title = { Text(text = "Confirm Delete") },
//            text = { Text(text = "Are you sure you want to delete this recipe?") },
//            confirmButton = {
//                Button(onClick = {
//                    recipeToDelete.value?.let {
//                        viewModel.OnEvent(RecipesEvent.DeleteRecipe(it.id))
//                    }
//                    showDialog.value = false
//                }) {
//                    Text("Yes")
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showDialog.value = false }) {
//                    Text("No")
//                }
//            }
//        )
//    }

    if (showSnackbar.value) {
        Snackbar(
            modifier = Modifier.padding(8.dp),
            action = {
                TextButton(onClick = {
                    Log.d("Clicked","I clicked")
                    viewModel.OnEvent(RecipesEvent.GetRecipes)
                    checkInternetAndUpdateSnackbar()
                }) {
                    Text("Reload")
                }
            },
        ) {
            Text("You are offline")
        }
    }
}
