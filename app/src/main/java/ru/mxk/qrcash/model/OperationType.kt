package ru.mxk.qrcash.model

enum class OperationType(value: String, description: String) {
    WITHDRAW("cashWithDrawal", "Выдача наличных"),
    DEPOSIT("cashDeposit", "Внесение наличных")
}