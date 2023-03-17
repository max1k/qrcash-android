package ru.mxk.qrcash.model

enum class CreateScreenStatus(val canLoadCards: Boolean, val canCreateOperation: Boolean) {
    INITIALIZING(canLoadCards = true, canCreateOperation = false),
    CARD_LIST_LOADING(canLoadCards = false, canCreateOperation = false),
    CARD_LIST_LOADED(canLoadCards = false, canCreateOperation = true),
    OPERATION_CREATION(canLoadCards = false, canCreateOperation = false),
    OPERATION_CREATED(canLoadCards = false, canCreateOperation = true),
    ERROR(canLoadCards = false, canCreateOperation = false);
}
