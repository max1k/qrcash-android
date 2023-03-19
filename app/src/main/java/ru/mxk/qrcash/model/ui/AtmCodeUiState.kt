package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.ui.enumeration.CodeCheckStatus

data class AtmCodeUiState(
    val code: String,
    val attempts: Int?,
    val status: CodeCheckStatus = CodeCheckStatus.INIT
)