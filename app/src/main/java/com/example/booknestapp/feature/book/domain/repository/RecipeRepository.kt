package com.example.booknestapp.feature.book.domain.repository

import com.example.booknestapp.feature.book.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getRecipes(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Int): Recipe?

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecepyById(id: Int?)

//    fun onDestroy()
}