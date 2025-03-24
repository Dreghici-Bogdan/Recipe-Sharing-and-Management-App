package com.example.booknestapp.feature.book.server

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Recipe(
    val id: Int?,
    val date: String,
    val title: String,
    val ingredients: String,        // New field
    val category: String,            // New field
    val rating: Double      // New field
)


interface RecipeApiService {
    // Fetch all recipes
    @GET("/recipes")
    fun getRecipes(): Call<List<Recipe>>

    // Fetch a single recipe by ID
    @GET("/recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

    // Add a new recipe
    @POST("/recipe")
    fun addRecipe(@Body recipe: Recipe): Call<Recipe>

    // Delete a recipe by ID
    @DELETE("/recipe/{id}")
    fun deleteRecipe(@Path("id") id: Int?): Call<Void>
}
