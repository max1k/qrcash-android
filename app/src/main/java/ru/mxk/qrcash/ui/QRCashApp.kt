package ru.mxk.qrcash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.ui.deposit.DepositScreen
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.navigation.QRCashScreen
import ru.mxk.qrcash.ui.withdraw.ATMCodeScreen
import ru.mxk.qrcash.ui.withdraw.WithdrawalScreen
import ru.mxk.qrcash.viewmodel.OperationCreationViewModel

@Composable
fun QRCashApp(
    modifier: Modifier = Modifier,
    creationViewModel: OperationCreationViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val creationUiState by creationViewModel.uiState.collectAsState()
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
                        onCreated = { navController.navigate(QRCashScreen.ATM_CODE.url) }
                    )
                },
                onNavigateBack = { resetToStart(creationViewModel, navController) }
            )
        }

        composable(route = QRCashScreen.ERROR.url) {
            OperationErrorScreen(
                creationUiState.type,
                onDoneClick = { resetToStart(creationViewModel, navController) }
            )
        }

        composable(route = QRCashScreen.ATM_CODE.url) {
            ATMCodeScreen(
                operation = creationUiState.operation!!
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
    navController: NavHostController
) {
    navController.popBackStack(QRCashScreen.OPERATION_CHOOSE.url, inclusive = false)
    operationCreationViewModel.reset()
}
