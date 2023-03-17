package ru.mxk.qrcash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.ui.deposit.DepositScreen
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.navigation.QRCashScreen
import ru.mxk.qrcash.ui.withdraw.AtmCodeInputScreen
import ru.mxk.qrcash.ui.withdraw.WithdrawalScreen
import ru.mxk.qrcash.viewmodel.AtmCodeViewModel
import ru.mxk.qrcash.viewmodel.OperationCreationViewModel

@Composable
fun QRCashApp(
    creationViewModel: OperationCreationViewModel,
    atmCodeViewModel: AtmCodeViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val creationUiState by creationViewModel.uiState.collectAsState()
    val atmCodeUiState by atmCodeViewModel.uiState.collectAsState()
    val sessionData = SessionData("111", "222", "3333")

    NavHost(
        navController = navController,
        startDestination = QRCashScreen.OPERATION_CHOOSE.url
    ) {
        composable(route = QRCashScreen.OPERATION_CHOOSE.url) {
            OperationChooseScreen(
                onWithdrawClick = { navController.navigate(QRCashScreen.WITHDRAWAL.url) },
                onDepositClick = { navController.navigate(QRCashScreen.DEPOSIT.url) }
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
                            atmCodeViewModel.operation = creationUiState.operation!!
                            navController.navigate(QRCashScreen.ATM_CODE.url)
                        }
                    )
                },
                onNavigateBack = { resetToStart(creationViewModel, atmCodeViewModel, navController) }
            )
        }

        composable(route = QRCashScreen.ERROR.url) {
            OperationErrorScreen(
                creationUiState.type,
                onDoneClick = { resetToStart(creationViewModel, atmCodeViewModel, navController) }
            )
        }

        composable(route = QRCashScreen.ATM_CODE.url) {
            AtmCodeInputScreen(
                uiState = atmCodeUiState,
                onCodeChange = { code ->
                    atmCodeViewModel.onCodeChange(code)
                    atmCodeViewModel.checkCode(sessionData) {
                        navController.navigate(QRCashScreen.CONFIRM.url)
                    }
                },
                onClose = { resetToStart(creationViewModel, atmCodeViewModel, navController) }
            )
        }

        composable(route = QRCashScreen.DEPOSIT.url) {
            creationViewModel.requestCardList(sessionData = sessionData)
            creationViewModel.setOperationType(OperationType.DEPOSIT)

            DepositScreen()
        }
    }
}

private fun resetToStart(
    operationCreationViewModel: OperationCreationViewModel,
    atmCodeViewModel: AtmCodeViewModel,
    navController: NavHostController
) {
    navController.popBackStack(QRCashScreen.OPERATION_CHOOSE.url, inclusive = false)
    operationCreationViewModel.reset()
    atmCodeViewModel.reset()
}
