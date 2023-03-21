package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import ru.mxk.qrcash.model.OperationWithCommission

class OperationViewModel: ViewModel(), Statused {

    var operation: OperationWithCommission? = null
        private set


    fun setOperation(operation: OperationWithCommission) {
        this.operation = operation
    }

    override fun reset() {
        operation = null
    }
}
