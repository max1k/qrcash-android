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
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.ViewModelStatus
import ru.mxk.qrcash.model.ui.CardListUiState
import ru.mxk.qrcash.service.QRCashService
import ru.mxk.qrcash.service.converter.FailedResponseException
import ru.mxk.qrcash.service.converter.getData
import kotlin.coroutines.CoroutineContext

class CardSelectionViewModel(private val qrCashService: QRCashService): ViewModel(), CoroutineScope {
    private val _uiState = MutableStateFlow(CardListUiState(selectedCard = null, cardList = null))
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun requestCardList(sessionData: SessionData) {
        if (!uiState.value.status.canBeReprocessed) {
            return
        }

        updateStatus(ViewModelStatus.LOADING)

        launch {
            val response = qrCashService.start(sessionData)
            try {
                val cards = response.getData().accounts.flatMap { it.cards }
                _uiState.update { currentState ->
                    currentState.copy(
                        cardList = cards,
                        selectedCard = cards.first(),
                        status = ViewModelStatus.DONE
                    )
                }
            } catch (exception: FailedResponseException) {
                updateStatus(ViewModelStatus.ERROR)
            }
        }
    }

    private fun updateStatus(status: ViewModelStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

}