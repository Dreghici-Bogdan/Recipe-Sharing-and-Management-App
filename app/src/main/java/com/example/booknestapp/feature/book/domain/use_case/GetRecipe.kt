package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Recipe
import com.example.booknestapp.feature.book.domain.repository.RecipeRepository

class GetRecipe(
    private val repository: RecipeRepository
) {

    suspend operator fun invoke(id: Int): Recipe? {
        return repository.getRecipeById(id)
    }
}