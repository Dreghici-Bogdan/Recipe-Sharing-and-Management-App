package com.example.booknestapp.feature.book.presentation.books

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknestapp.feature.book.domain.use_case.REcipeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeUseCases: REcipeUseCases
): ViewModel() {

    private val _state = mutableStateOf(RecipesState())
    val state: State<RecipesState> = _state


    private val _hasFetchedRecipes = mutableStateOf(false)
    val hasFetchedRecipes: State<Boolean> = _hasFetchedRecipes

    init {
        getRecipes()
    }

    fun OnEvent(event: RecipesEvent){
        when(event) {
            is RecipesEvent.DeleteRecipe -> {
                viewModelScope.launch{
                    recipeUseCases.deleteRecipe(event.recipeId)
                }
            }
            is RecipesEvent.GetRecipes -> {
                // Trigger fetching recipes
                getRecipes()
            }
        }
    }

    private fun getRecipes() {
        recipeUseCases.getRecipes()
            .onEach { recipes ->
                _state.value = state.value.copy(
                    recipes = recipes
                )
                if(recipes.size != 0) {
                    _hasFetchedRecipes.value = true
                }
                Log.d("Clicked","I clicked got recipes $recipes")
            }
            .launchIn(viewModelScope)
    }

}