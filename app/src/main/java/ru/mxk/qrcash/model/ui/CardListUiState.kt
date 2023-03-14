package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.ViewModelStatus

data class CardListUiState(
    val selectedCard: Card?,
    val cardList: List<Card>?,
    val status: ViewModelStatus = ViewModelStatus.INITIALIZING,
) {
    val isLoading = status == ViewModelStatus.LOADING
}