package com.cashproject.mongsil.kmp.core.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

@Composable
fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInAccount?) -> Unit,
    onError: (String) -> Unit = {},
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            onResult(account)
        } catch (e: ApiException) {
            val message = getSignInErrorMessage(e.statusCode)
            onError(message)
        }
    }

    return {
        val client = GoogleSignIn.getClient(context, GoogleDriveBackupService.buildSignInOptions())
        launcher.launch(client.signInIntent)
    }
}

private fun getSignInErrorMessage(statusCode: Int): String {
    return when (statusCode) {
        CommonStatusCodes.SIGN_IN_REQUIRED ->
            "로그인이 필요합니다. 다시 시도해주세요."

        CommonStatusCodes.NETWORK_ERROR ->
            "네트워크 연결을 확인해주세요."

        CommonStatusCodes.CANCELED ->
            "로그인이 취소되었습니다."

        12501 ->
            "로그인이 취소되었습니다."

        12502 ->
            "로그인 진행 중입니다. 잠시 후 다시 시도해주세요."

        CommonStatusCodes.DEVELOPER_ERROR ->
            "앱 설정 오류가 발생했습니다. (코드: ${CommonStatusCodes.getStatusCodeString(statusCode)})"

        else ->
            "Google 로그인에 실패했습니다. (코드: ${CommonStatusCodes.getStatusCodeString(statusCode)})"
    }
}
