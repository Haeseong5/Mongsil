package com.cashproject.mongsil.kmp.screen.diarywrite.component

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme

/**
 * 작성 중인 내용을 버리고 나갈 것인지 확인하는 다이얼로그
 *
 * @param onConfirm 확인 버튼 클릭 콜백
 * @param onDismiss 취소 또는 다이얼로그 닫기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = "작성 중인 내용이 있습니다",
                    style = MongsilTheme.typography.headline1.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "지금 화면을 벗어나면 작성 중인 모든 정보가 지워집니다.",
                    style = MongsilTheme.typography.body1Normal.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF6200EE)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = "취소",
                            style = MongsilTheme.typography.body1Bold.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "확인",
                            style = MongsilTheme.typography.body1Bold.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
