package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import ru.mxk.qrcash.model.OperationWithCommission

class OperationViewModel: ViewModel(), Statused {
    private var _operation: OperationWithCommission? = null

    val operation: OperationWithCommission
        get() = _operation!!


    fun setOperation(operation: OperationWithCommission) {
        _operation = operation
    }

    override fun reset() {
        _operation = null
    }
}
