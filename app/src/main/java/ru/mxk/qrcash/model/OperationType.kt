package ru.mxk.qrcash.model

import com.google.gson.annotations.SerializedName

enum class OperationType(val description: String, val errorDescription: String) {
    @SerializedName("cashWithDrawal")
    WITHDRAW("Выдача наличных", "Невозможно снять наличные"),
    @SerializedName("cashDeposit")
    DEPOSIT("Внесение наличных", "Невозможно внести наличные");
}