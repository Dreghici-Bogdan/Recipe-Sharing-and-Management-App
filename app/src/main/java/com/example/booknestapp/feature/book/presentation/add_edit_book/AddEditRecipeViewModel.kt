package com.example.booknestapp.feature.book.presentation.add_edit_book

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknestapp.feature.book.domain.model.Recipe
import com.example.booknestapp.feature.book.domain.model.InvalidRecipeException
import com.example.booknestapp.feature.book.domain.use_case.REcipeUseCases
import com.example.booknestapp.feature.book.presentation.add_edit_book.AddEditRecipeViewModel.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRecipeViewModel @Inject constructor (
    private val recipeUseCases: REcipeUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipeTitle = mutableStateOf(
        RecipeTextFieldState(
            hint = "Enter Title"
        )
    )
    val recipeTitle: State<RecipeTextFieldState> = _recipeTitle

    private val _recipeYear = mutableStateOf(
        RecipeTextFieldState(
            hint = "Enter Year"
        )
    )
    val recipeAuthor: State<RecipeTextFieldState> = _recipeYear

    private val _recipeCategory = mutableStateOf(
        RecipeTextFieldState(
            hint = "Enter Category"
        )
    )
    val recipeCategory: State<RecipeTextFieldState> = _recipeCategory

    private val _recipeIngredients = mutableStateOf(
        RecipeTextFieldState(
            hint = "Enter Ingredients"
        )
    )
    val recipeIngredients: State<RecipeTextFieldState> = _recipeIngredients

//    private val _bookISBN = mutableStateOf(
//        RecipeTextFieldState(
//            hint = "Enter Category"
//        )
//    )
//    val bookISBN: State<RecipeTextFieldState> = _bookISBN

    private val _recipeRating = mutableStateOf(
        RecipeTextFieldState(
            hint = "Enter Rating"
        )
    )
    val recipeRating: State<RecipeTextFieldState> = _recipeRating

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _currentRecipeId: Int? = null

    init {
        savedStateHandle.get<Int>("recipeId")?.let { recipeId ->
            if(recipeId != -1) {
                viewModelScope.launch {
                    recipeUseCases.getRecipe(recipeId)?.also {
//                        id = this.id,
//                        date = this.date,
//                        title = this.title,
//                        ingradients = this.ingradients,
//                        category = this.category,
//                        rating = this.rating

                        _currentRecipeId = it.id
                        _recipeTitle.value = recipeTitle.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )
                        _recipeYear.value = recipeAuthor.value.copy(
                            text = it.date,
                            isHintVisible = false
                        )
                        _recipeIngredients.value = recipeIngredients.value.copy(
                            text = it.ingredients,
                            isHintVisible = false
                        )
                        _recipeCategory.value = recipeCategory.value.copy(
                            text = it.category,
                            isHintVisible = false
                        )
                        _recipeRating.value = recipeRating.value.copy(
                            text = it.rating.toString(),
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditRecipeEvent) {
        when(event) {
            is AddEditRecipeEvent.EnteredTitle -> {
                _recipeTitle.value = recipeTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditRecipeEvent.ChangeTitleFocus -> {
                _recipeTitle.value = recipeTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && recipeTitle.value.text.isBlank()
                )
            }
            is AddEditRecipeEvent.EnteredAuthor -> {
                _recipeYear.value = recipeAuthor.value.copy(
                    text = event.value
                )
            }
            is AddEditRecipeEvent.ChangeAuthorFocus -> {
                _recipeYear.value = recipeAuthor.value.copy(
                    isHintVisible = !event.focusState.isFocused && recipeAuthor.value.text.isBlank()
                )
            }
            is AddEditRecipeEvent.ChangeGenreFocus -> {
                _recipeCategory.value = recipeCategory.value.copy(
                    isHintVisible = !event.focusState.isFocused && recipeCategory.value.text.isBlank()
                )
            }
            is AddEditRecipeEvent.ChangeYearFocus -> {
                _recipeIngredients.value = recipeIngredients.value.copy(
                    isHintVisible = !event.focusState.isFocused && recipeIngredients.value.text.isBlank()
                )
            }
            is AddEditRecipeEvent.ChangeISBNFocus -> {
                _recipeCategory.value = recipeCategory.value.copy(
                    isHintVisible = !event.focusState.isFocused && recipeCategory.value.text.isBlank()
                )
            }
            is AddEditRecipeEvent.ChangeAvailabilityFocus -> {
                _recipeRating.value = recipeRating.value.copy(
                    isHintVisible = !event.focusState.isFocused && recipeRating.value.text.isBlank()
                )
            }

            is AddEditRecipeEvent.SaveRecipe -> {
                viewModelScope.launch{
                    try {
                        val year = recipeIngredients.value.text.toIntOrNull() // Safely convert to Int
                        recipeUseCases.addRecipe(
                            Recipe(
//                                id = this.id,
//                                date = this.date,
//                                title = this.title,
//                                ingradients = this.ingradients,
//                                category = this.category,
//                                rating = this.rating
                                date = recipeAuthor.value.text,
                                title = recipeTitle.value.text,
                                ingredients = recipeIngredients.value.text,
                                category = recipeCategory.value.text,
                                rating = recipeRating.value.text.toDouble(),
                                id = _currentRecipeId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveRecipe)
                    } catch(e: InvalidRecipeException) {
                        _eventFlow.emit(
                            ShowSnackBar(
                                message = e.message ?: "Couldn't save recipe"
                            )
                        )
                    }
                }
            }

            is AddEditRecipeEvent.EnteredAvailability -> {
                _recipeRating.value = recipeRating.value.copy(
                    text = event.value
                )
            }
            is AddEditRecipeEvent.EnteredGenre -> {
                _recipeCategory.value = recipeCategory.value.copy(
                    text = event.value
                )
            }
            is AddEditRecipeEvent.EnteredISBN -> {
                _recipeCategory.value = recipeCategory.value.copy(
                    text = event.value
                )
            }
            is AddEditRecipeEvent.EnteredYear -> {
                _recipeIngredients.value = recipeIngredients.value.copy(
                    text = event.value
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveRecipe: UiEvent()
    }

    public fun DeleteRecipe () {
        viewModelScope.launch{
            recipeUseCases.deleteRecipe(_currentRecipeId)
        }
    }
}