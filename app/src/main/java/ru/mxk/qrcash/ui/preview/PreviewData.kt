package ru.mxk.qrcash.ui.preview

import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.CardType
import ru.mxk.qrcash.model.Operation
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.CardListUiState
import ru.mxk.qrcash.model.ui.OperationUiState
import java.math.BigDecimal

val PREVIEW_CARD = Card(
    publicId = "123",
    name = "Цифровая карта",
    maskedNumber = "220024******0923",
    shortNumber = "0923",
    balance = BigDecimal(55000),
    paymentSystem = "MIR",
    systemPlacingName ="WWWF",
    cardType = CardType.PREPAID_CARD
)

val PREVIEW_OPERATION_UI_STATE = OperationUiState(
    BigDecimal.TEN,
    Operation(PREVIEW_CARD, OperationType.WITHDRAW, "", BigDecimal.ZERO),
    OperationType.WITHDRAW
)

val PREVIEW_CARD_LIST_UI_STATE = CardListUiState(
    selectedCard = PREVIEW_CARD,
    cardList = listOf(PREVIEW_CARD)
)