package ru.mxk.qrcash.model.ui.enumeration

enum class CodeCheckStatus(val canBeReprocessed: Boolean ) {
    INIT(true),
    LOADING(false),
    INVALID_CODE(true),
    DONE(false),
    ERROR(false),
}