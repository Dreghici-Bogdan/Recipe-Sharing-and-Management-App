package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Recipe
import com.example.booknestapp.feature.book.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipes(
    private val repository: RecipeRepository
){

    operator fun invoke() : Flow<List<Recipe>> {
        return repository.getRecipes()
    }
}