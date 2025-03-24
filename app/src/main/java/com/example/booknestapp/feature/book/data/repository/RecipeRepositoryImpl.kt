package com.example.booknestapp.feature.book.data.repository

import android.util.Log
import com.example.booknestapp.feature.book.data.source.RecipeDao
import com.example.booknestapp.feature.book.domain.model.Recipe
import com.example.booknestapp.feature.book.domain.repository.RecipeRepository
import com.example.booknestapp.feature.book.server.RecipeApiService
import com.example.booknestapp.feature.book.server.RetrofitClient
import com.example.booknestapp.feature.book.server.WebSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException

class RecipeRepositoryImpl(
    private val dao: RecipeDao,
    private val webSocketClient: WebSocketClient
) : RecipeRepository {

    val recipeService = RetrofitClient.instance.create(RecipeApiService::class.java)
    private val retryList = mutableListOf<Recipe>()
    private var retrying = false // To prevent multiple retry coroutines

    init {
        fetchAndSaveRecipes()
    }

    private fun setupWebSocketListener() {
        webSocketClient.startWebSocket()
    }

    private fun startRetrying() {
        if (retrying) return
        retrying = true

        GlobalScope.launch(Dispatchers.IO) {
            while (retryList.isNotEmpty()) {
                val iterator = retryList.iterator()
                while (iterator.hasNext()) {
                    val recipe = iterator.next()
                    try {
                        val serverRecipe = recipe.toServerModel()
                        val call = recipeService.addRecipe(serverRecipe)
                        val response = call.execute() // Synchronous call for retrying
                        if (response.isSuccessful) {
                            iterator.remove() // Remove successfully synced recipe
                            Log.d("RecipeRepository", "Successfully retried adding recipe: ${recipe.title}")
                        } else {
                            Log.e("RecipeRepository", "Failed to retry adding recipe: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("RecipeRepository", "Error during retrying: ${e.message}")
                    }
                }
                delay(5000) // Wait 5 seconds before retrying
            }
            retrying = false // Stop retrying when the list is empty
        }
    }

    fun fetchAndSaveRecipes() {
        setupWebSocketListener()
        val call = recipeService.getRecipes()

        call.enqueue(object : Callback<List<com.example.booknestapp.feature.book.server.Recipe>> {
            override fun onResponse(
                call: Call<List<com.example.booknestapp.feature.book.server.Recipe>>,
                response: Response<List<com.example.booknestapp.feature.book.server.Recipe>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        Log.d("Clicked", "Recipes fetched: $recipes")
                        GlobalScope.launch(Dispatchers.IO) {
                            deleteAllRecipes()
                            insertFetchedRecipes(recipes)
                        }
                    }
                } else {
                    Log.e("RecipeRepository", "Failed to fetch recipes from server")
                }
            }

            override fun onFailure(call: Call<List<com.example.booknestapp.feature.book.server.Recipe>>, t: Throwable) {
                Log.e("RecipeRepository", "Error fetching recipes: ${t.message}")
            }
        })
    }

    private fun deleteAllRecipes() {
        dao.deleteAllRecipes()
    }

    private suspend fun insertFetchedRecipes(recipes: List<com.example.booknestapp.feature.book.server.Recipe>) {
        withContext(Dispatchers.IO) {
            recipes.forEach { recipe ->
                val localRecipe = recipe.toDomainModel()
                dao.insertRecipe(localRecipe)
                Log.d("RecipeRepository", "Fetched $recipes")
            }
        }
    }

    private fun com.example.booknestapp.feature.book.server.Recipe.toDomainModel(): Recipe {
        return Recipe(
            id = this.id,
            date = this.date,
            title = this.title,
            ingredients = this.ingredients,
            category = this.category,
            rating = this.rating
        )
    }

    private fun Recipe.toServerModel(): com.example.booknestapp.feature.book.server.Recipe {
        return com.example.booknestapp.feature.book.server.Recipe(
            id = this.id,
            date = this.date,
            title = this.title,
            ingredients = this.ingredients,
            category = this.category,
            rating = this.rating
        )
    }

    fun checkSizeDaoRecipes(): Int {
        var sizeOfRecipes = 0
        dao.getRecipes().onEach { recipes ->
            sizeOfRecipes = recipes.size
        }
        return sizeOfRecipes
    }

    override fun getRecipes(): Flow<List<Recipe>> {
        val sizee = checkSizeDaoRecipes()
        if (sizee == 0) {
            fetchAndSaveRecipes()
        }
        return dao.getRecipes()
    }

    override suspend fun getRecipeById(id: Int): Recipe? {
        return suspendCancellableCoroutine { continuation ->
            val call = recipeService.getRecipeById(id)
            call.enqueue(object : Callback<com.example.booknestapp.feature.book.server.Recipe> {
                override fun onResponse(
                    call: Call<com.example.booknestapp.feature.book.server.Recipe?>,
                    response: Response<com.example.booknestapp.feature.book.server.Recipe?>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()?.toDomainModel(), onCancellation = null)
                    } else {
                        continuation.resumeWithException(Exception("Failed to retrieve recipe"))
                    }
                }

                override fun onFailure(call: Call<com.example.booknestapp.feature.book.server.Recipe>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        val newId = (dao.getMaxRecipeId() ?: 0) + 1
        val newRecipe = recipe.copy(id = newId)
        dao.insertRecipe(newRecipe)

        val serverRecipe = newRecipe.toServerModel()
        val call = recipeService.addRecipe(serverRecipe)
        call.enqueue(object : Callback<com.example.booknestapp.feature.book.server.Recipe> {
            override fun onResponse(
                call: Call<com.example.booknestapp.feature.book.server.Recipe>,
                response: Response<com.example.booknestapp.feature.book.server.Recipe>
            ) {
                if (response.isSuccessful) {
                    Log.d("RecipeRepository", "Recipe added successfully to the server.")
                } else {
                    Log.e("RecipeRepository", "Failed to add recipe to the server.")
                }
            }

            override fun onFailure(call: Call<com.example.booknestapp.feature.book.server.Recipe>, t: Throwable) {
                retryList.add(newRecipe)
                startRetrying()
            }
        })
    }

    override suspend fun deleteRecepyById(id: Int?) {
        dao.deleteRecipeById(id)
        val call = recipeService.deleteRecipe(id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("RecipeRepository", "Successfully deleted recipe from the server.")
                } else {
                    Log.e("RecipeRepository", "Failed to delete recipe from the server.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RecipeRepository", "Error deleting recipe from server: ${t.message}")
            }
        })
    }
}
