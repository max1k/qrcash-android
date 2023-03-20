package ru.mxk.qrcash.ui.preview

import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.CardType
import ru.mxk.qrcash.model.Operation
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.OperationWithCommission
import ru.mxk.qrcash.model.ui.OperationCreationUiState
import ru.mxk.qrcash.model.ui.enumeration.CreateScreenStatus
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

val PREVIEW_CARD_LIST_UI_STATE = OperationCreationUiState(
    selectedCard = PREVIEW_CARD,
    cardList = listOf(PREVIEW_CARD),
    amount = BigDecimal(10),
    operation = null,
    type = OperationType.WITHDRAW,
    status = CreateScreenStatus.CARD_LIST_LOADED
)

val PREVIEW_OPERATION = OperationWithCommission(
    operation = Operation(
        card = PREVIEW_CARD,
        type = OperationType.WITHDRAW,
        orderId = "111",
        amount = BigDecimal(5000)
    ),
    commission = BigDecimal.ZERO
)