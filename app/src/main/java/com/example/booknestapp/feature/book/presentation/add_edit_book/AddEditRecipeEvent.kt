package com.example.booknestapp.feature.book.presentation.add_edit_book

import androidx.compose.ui.focus.FocusState

sealed class AddEditRecipeEvent {
    data class EnteredTitle(val value: String): AddEditRecipeEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditRecipeEvent()

    data class EnteredAuthor(val value: String): AddEditRecipeEvent()
    data class ChangeAuthorFocus(val focusState: FocusState): AddEditRecipeEvent()

    data class EnteredGenre(val value: String): AddEditRecipeEvent()
    data class ChangeGenreFocus(val focusState: FocusState): AddEditRecipeEvent()

    data class EnteredYear(val value: String): AddEditRecipeEvent()
    data class ChangeYearFocus(val focusState: FocusState): AddEditRecipeEvent()

    data class EnteredISBN(val value: String): AddEditRecipeEvent()
    data class ChangeISBNFocus(val focusState: FocusState): AddEditRecipeEvent()

    data class EnteredAvailability(val value: String): AddEditRecipeEvent()
    data class ChangeAvailabilityFocus(val focusState: FocusState): AddEditRecipeEvent()

    object SaveRecipe: AddEditRecipeEvent()
}