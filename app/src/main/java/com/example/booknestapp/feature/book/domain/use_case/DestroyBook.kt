package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.repository.RecipeRepository

class DestroyBook (
    private val repository: RecipeRepository
){
//    operator fun invoke(id: Int){
//        repository.onDestroy()
//    }
}