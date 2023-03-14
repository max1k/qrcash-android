package ru.mxk.qrcash.model.dto

import ru.mxk.qrcash.model.Card

data class AccountSummary (
    val accounts: List<Account>
)

data class Account (
    val accountNumber: String,
    val currency: CurrencyType,
    val cards: List<Card>
)

enum class CurrencyType {
    RUB,
    USD,
    EUR
}



