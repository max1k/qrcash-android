package ru.mxk.qrcash.model

enum class ViewModelStatus(val canBeReprocessed: Boolean) {
    INITIALIZING(canBeReprocessed = true),
    LOADING(canBeReprocessed = false),
    DONE(canBeReprocessed = false),
    ERROR(canBeReprocessed = true);
}
