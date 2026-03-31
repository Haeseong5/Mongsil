package com.cashproject.mongsil.kmp.core.backup

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cashproject.mongsil.kmp.R
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
            val message = getSignInErrorMessage(context, e.statusCode)
            onError(message)
        }
    }

    return {
        val client = GoogleSignIn.getClient(context, GoogleDriveBackupService.buildSignInOptions())
        launcher.launch(client.signInIntent)
    }
}

private fun getSignInErrorMessage(context: Context, statusCode: Int): String {
    return when (statusCode) {
        CommonStatusCodes.SIGN_IN_REQUIRED ->
            context.getString(R.string.sign_in_error_required)

        CommonStatusCodes.NETWORK_ERROR ->
            context.getString(R.string.sign_in_error_network)

        CommonStatusCodes.CANCELED, 12501 ->
            context.getString(R.string.sign_in_error_canceled)

        12502 ->
            context.getString(R.string.sign_in_error_in_progress)

        CommonStatusCodes.DEVELOPER_ERROR ->
            context.getString(R.string.sign_in_error_developer, CommonStatusCodes.getStatusCodeString(statusCode))

        else ->
            context.getString(R.string.sign_in_error_unknown, CommonStatusCodes.getStatusCodeString(statusCode))
    }
}
