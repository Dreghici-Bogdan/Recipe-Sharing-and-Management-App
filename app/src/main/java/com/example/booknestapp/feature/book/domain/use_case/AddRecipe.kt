package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Recipe
import com.example.booknestapp.feature.book.domain.model.InvalidRecipeException
import com.example.booknestapp.feature.book.domain.repository.RecipeRepository
import kotlin.jvm.Throws

class AddRecipe (
    private val repository: RecipeRepository
) {

    @Throws(InvalidRecipeException::class)
    suspend operator fun invoke(recipe: Recipe) {

        repository.insertRecipe(recipe)
    }

}