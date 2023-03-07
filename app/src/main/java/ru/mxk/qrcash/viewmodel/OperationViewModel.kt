package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mxk.qrcash.model.OperationUiState

class OperationViewModel: ViewModel() {
    private val _uistate = MutableStateFlow(OperationUiState(""))
}