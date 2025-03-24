package com.example.booknestapp.feature.book.presentation.books

import com.example.booknestapp.feature.book.domain.model.Recipe

data class RecipesState (
    val recipes: List<Recipe> = emptyList()
)