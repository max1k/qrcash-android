package ru.mxk.qrcash.ui.navigation

enum class QRCashScreen(val url: String) {
    OPERATION_CHOOSE("operation_choose"),
    WITHDRAWAL("withdrawal"),
    DEPOSIT("deposit"),
    ATM_CODE("atm_code"),
    CONFIRM("confirm"),
    ERROR("error")
}