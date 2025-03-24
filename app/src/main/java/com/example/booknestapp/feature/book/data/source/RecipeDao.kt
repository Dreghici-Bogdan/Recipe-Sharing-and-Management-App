package com.example.booknestapp.feature.book.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booknestapp.feature.book.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    suspend fun getRecipeById(id :Int): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    // New method to delete a Recipe based on its id
    @Query("DELETE FROM recipe WHERE id = :id")
    suspend fun deleteRecipeById(id: Int?)

    @Query("DELETE FROM recipe")
    fun deleteAllRecipes()

    @Query("SELECT MAX(id) FROM recipe")
    suspend fun getMaxRecipeId(): Int?
}