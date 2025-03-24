package com.example.booknestapp.feature.book.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booknestapp.feature.book.domain.model.Recipe

@Database(
    entities = [Recipe::class],
    version = 5,
    exportSchema = false
)

abstract class RecipeDataBase : RoomDatabase(){

    abstract val recipeDao: RecipeDao

    companion object{
        const val DATABASE_NAME = "recipe_db"
    }
}