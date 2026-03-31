package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.app_update_dialog_message
import mongsil.composeapp.generated.resources.app_update_dialog_title
import mongsil.composeapp.generated.resources.app_update_button
import mongsil.composeapp.generated.resources.dialog_cancel
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUpdateDialog(
    currentVersion: String,
    latestVersion: String,
    onUpdate: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(Res.string.app_update_dialog_title),
                    style = MongsilTheme.typography.headline1.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(
                        Res.string.app_update_dialog_message,
                        currentVersion,
                        latestVersion,
                    ),
                    style = MongsilTheme.typography.body1Normal.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                    ),
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF6200EE),
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                        ),
                    ) {
                        Text(
                            text = stringResource(Res.string.dialog_cancel),
                            style = MongsilTheme.typography.body1Bold.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onUpdate,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE),
                            contentColor = Color.White,
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = stringResource(Res.string.app_update_button),
                            style = MongsilTheme.typography.body1Bold.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                            ),
                        )
                    }
                }
            }
        }
    }
}
