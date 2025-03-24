package com.example.booknestapp.feature.book.presentation.add_edit_book

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.booknestapp.feature.book.presentation.add_edit_book.components.TransparentHintTextField
import com.example.booknestapp.feature.book.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditRecipeScreen(
    navController: NavController,
    viewModel: AddEditRecipeViewModel = hiltViewModel()
) {
    val titleState = viewModel.recipeTitle.value
    val authorState = viewModel.recipeAuthor.value
    val ingredientsState = viewModel.recipeIngredients.value
    val categoryState = viewModel.recipeCategory.value
    val ratingState = viewModel.recipeRating.value
    val scaffoldState = rememberScaffoldState()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditRecipeViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is AddEditRecipeViewModel.UiEvent.SaveRecipe -> {
                    navController.navigate(Screen.RecipesScreen.route)
                }
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add/Edit Recipe") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditRecipeEvent.SaveRecipe)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Save recipe")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            // Title label and text field
            Text(
                text = "Title",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditRecipeEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditRecipeEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Year",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = authorState.text,
                hint = authorState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditRecipeEvent.EnteredAuthor(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditRecipeEvent.ChangeAuthorFocus(it))
                },
                isHintVisible = authorState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = ingredientsState.text,
                hint = ingredientsState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditRecipeEvent.EnteredYear(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditRecipeEvent.ChangeYearFocus(it))
                },
                isHintVisible = ingredientsState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Category",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = categoryState.text,
                hint = categoryState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditRecipeEvent.EnteredISBN(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditRecipeEvent.ChangeISBNFocus(it))
                },
                isHintVisible = categoryState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Rating",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = ratingState.text,
                hint = ratingState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditRecipeEvent.EnteredAvailability(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditRecipeEvent.ChangeAvailabilityFocus(it))
                },
                isHintVisible = ratingState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))
            IconButton(
                onClick = {
                    showDialog.value = true
                },
                Modifier.padding(bottom = 8.dp)
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete recipe"
                )
            }

        }

    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { androidx.compose.material.Text(text = "Confirm Delete") },
            text = { androidx.compose.material.Text(text = "Are you sure you want to delete this recipe?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.DeleteRecipe() // Call delete function
                    showDialog.value = false
                    navController.navigate(Screen.RecipesScreen.route)
                }) {
                    androidx.compose.material.Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    androidx.compose.material.Text("No")
                }
            }
        )
    }
}
