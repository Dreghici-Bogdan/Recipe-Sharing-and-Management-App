package com.example.booknestapp.feature.book.presentation.books

sealed class RecipesEvent {
    data class DeleteRecipe(val recipeId: Int?): RecipesEvent()
    object GetRecipes : RecipesEvent()
}