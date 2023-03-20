package ru.mxk.qrcash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.OperationWithCommission
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.ui.enumeration.CodeInputType
import ru.mxk.qrcash.ui.deposit.DepositScreen
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.navigation.QRCashScreen
import ru.mxk.qrcash.ui.withdraw.CodeInputScreen
import ru.mxk.qrcash.ui.withdraw.WithdrawalConfirmScreen
import ru.mxk.qrcash.ui.withdraw.WithdrawalScreen
import ru.mxk.qrcash.viewmodel.AtmCodeViewModel
import ru.mxk.qrcash.viewmodel.ConfirmViewModel
import ru.mxk.qrcash.viewmodel.OperationCreationViewModel
import ru.mxk.qrcash.viewmodel.Statused

@Composable
fun QRCashApp(
    creationViewModel: OperationCreationViewModel,
    atmCodeViewModel: AtmCodeViewModel,
    confirmViewModel: ConfirmViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val viewModels = listOf<Statused>(
        creationViewModel,
        atmCodeViewModel,
        confirmViewModel
    )
    val creationUiState by creationViewModel.uiState.collectAsState()
    val atmCodeUiState by atmCodeViewModel.uiState.collectAsState()
    val confirmUiState by confirmViewModel.uiState.collectAsState()

    val sessionData = SessionData("111", "222", "3333")
    val resetToStart = { resetToStart(viewModels, navController) }

    NavHost(
        navController = navController,
        startDestination = QRCashScreen.OPERATION_CHOOSE.url
    ) {
        composable(route = QRCashScreen.OPERATION_CHOOSE.url) {
            OperationChooseScreen(
                onWithdrawClick = { navController.navigate(QRCashScreen.WITHDRAWAL) },
                onDepositClick = { navController.navigate(QRCashScreen.DEPOSIT) }
            )
        }

        composable(route = QRCashScreen.WITHDRAWAL.url) {
            creationViewModel.requestCardList(sessionData = sessionData)
            creationViewModel.setOperationType(OperationType.WITHDRAW)

            WithdrawalScreen(
                creationUiState = creationUiState,
                onAmountChange = { creationViewModel.setAmount(it) },
                onCreateOperation = {
                    creationViewModel.createOperation(
                        sessionData,
                        creationUiState.selectedCard!!,
                        onCreated = {
                            navController.navigate(QRCashScreen.ATM_CODE)
                        }
                    )
                },
                onNavigateBack = resetToStart
            )
        }

        composable(route = QRCashScreen.ERROR.url) {
            OperationErrorScreen(
                creationUiState.type,
                onDoneClick = resetToStart
            )
        }

        composable(route = QRCashScreen.ATM_CODE.url) {
            atmCodeViewModel.operation = creationUiState.operation ?: return@composable

            CodeInputScreen(
                type = CodeInputType.ATM_CODE,
                uiState = atmCodeUiState,
                onCodeChange = { code ->
                    atmCodeViewModel.onCodeChange(code)
                    atmCodeViewModel.checkCode(sessionData) {
                        confirmViewModel.operation = OperationWithCommission(
                            creationUiState.operation!!,
                            commission = atmCodeViewModel.commission
                        )

                        navController.navigate(QRCashScreen.CONFIRM)
                    }
                },
                onClose = resetToStart,
            )
        }

        composable(route = QRCashScreen.CONFIRM.url) {
            WithdrawalConfirmScreen(
                confirmViewModel.operation,
                sessionData = sessionData,
                uiState = confirmUiState,
                onConfirm = {
                    confirmViewModel.confirm(sessionData) {
                        navController.navigate(QRCashScreen.OTP_CODE)
                    }
                },
                onClose = resetToStart
            )
        }

        composable(route = QRCashScreen.OTP_CODE.url) {

        }

        composable(route = QRCashScreen.DEPOSIT.url) {
            creationViewModel.requestCardList(sessionData = sessionData)
            creationViewModel.setOperationType(OperationType.DEPOSIT)

            DepositScreen()
        }
    }
}

private fun resetToStart(viewModels: List<Statused>, navController: NavHostController) {
    navController.popBackStack(QRCashScreen.OPERATION_CHOOSE.url, inclusive = false)
    viewModels.forEach(Statused::reset)
}

private fun NavHostController.navigate(screen: QRCashScreen) {
    navigate(screen.url)
}
