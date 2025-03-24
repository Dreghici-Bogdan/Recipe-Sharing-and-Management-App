package com.example.booknestapp.feature.book.domain.use_case

data class REcipeUseCases (
    val getRecipes: GetRecipes,
    val deleteRecipe: DeleteRecipe,
    val addRecipe: AddRecipe,
    val getRecipe: GetRecipe,
//    val destroyBook: DestroyBook
)