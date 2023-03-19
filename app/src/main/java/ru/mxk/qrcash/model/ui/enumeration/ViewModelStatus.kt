package ru.mxk.qrcash.model.ui.enumeration

enum class ViewModelStatus(val canBeReprocessed: Boolean) {
    INIT(true),
    LOADING(false),
    DONE(false),
    ERROR(false)
}