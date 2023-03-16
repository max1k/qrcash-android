package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.ViewModelStatus
import ru.mxk.qrcash.model.ui.CardListUiState
import ru.mxk.qrcash.service.QRCashService
import kotlin.coroutines.CoroutineContext

class CardSelectionViewModel(private val qrCashService: QRCashService): ViewModel(), CoroutineScope {
    private val _uiState = MutableStateFlow(CardListUiState(selectedCard = null, cardList = null))
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                cardList = null,
                selectedCard = null,
                status = ViewModelStatus.INITIALIZING
            )
        }
    }

    fun requestCardList(sessionData: SessionData, onError: () -> Unit) {
        if (!uiState.value.status.canBeReprocessed) {
            return
        }

        updateStatus(ViewModelStatus.LOADING)

        launch {
            val result = qrCashService.start(sessionData)
                .map { it.accounts.flatMap { account -> account.cards } }

            if (result.isPresent()) {
                val cards = result.data
                _uiState.update { currentState ->
                    currentState.copy(
                        cardList = cards,
                        selectedCard = cards.first(),
                        status = ViewModelStatus.DONE
                    )
                }
            } else {
                updateStatus(ViewModelStatus.ERROR)
                withContext(Dispatchers.Main) {
                    onError()
                }
            }
        }
    }

    private fun updateStatus(status: ViewModelStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

}