package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.ViewModelStatus

data class AtmCodeUiState(
    val code: String,
    val status: ViewModelStatus = ViewModelStatus.INIT
)