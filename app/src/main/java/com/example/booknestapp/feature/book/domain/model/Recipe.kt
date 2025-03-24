package com.example.booknestapp.feature.book.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    val date: String,
    val title: String,
    val ingredients: String,        // New field
    val category: String,            // New field
    val rating: Double,         // New field
    @PrimaryKey val id: Int? = null
)

class InvalidRecipeException(message: String) : Exception(message)
