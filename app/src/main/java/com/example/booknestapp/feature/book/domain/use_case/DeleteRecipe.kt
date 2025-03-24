package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.repository.RecipeRepository

class DeleteRecipe(
   private val repository: RecipeRepository
) {

    suspend operator fun invoke(id: Int?) {
        repository.deleteRecepyById(id)
    }
}