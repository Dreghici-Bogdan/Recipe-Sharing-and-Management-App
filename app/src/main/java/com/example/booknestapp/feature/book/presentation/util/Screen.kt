package com.example.booknestapp.feature.book.presentation.util

sealed class Screen(val route: String) {
    object RecipesScreen : Screen("books_screen")
    object AddEditRecipeScreen: Screen("add_edit_book_screen")
}