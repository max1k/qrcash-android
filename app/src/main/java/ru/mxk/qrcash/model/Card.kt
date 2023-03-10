package ru.mxk.qrcash.model

import java.math.BigDecimal

data class Card (
    val publicId: String,
    val name: String,
    val maskedNumber: String,
    val shortNumber: String,
    val balance: BigDecimal,
    val paymentSystem: String,
    val systemPlacingName: String,
    val cardType: CardType
)

enum class CardType {
    MASTER_ACCOUNT,
    CURRENT_ACCOUNT,
    DEBET_CARD,
    CREDIT_CARD,
    PREPAID_CARD
}
