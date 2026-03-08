package com.cashproject.mongsil.kmp.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_archive
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_language
import mongsil.composeapp.generated.resources.ic_lock
import mongsil.composeapp.generated.resources.ic_menu
import mongsil.composeapp.generated.resources.ic_notifications
import mongsil.composeapp.generated.resources.ic_shopping_bag
import mongsil.composeapp.generated.resources.ic_upload
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingScreen(
    onBack: () -> Unit = {},
    onNavigateToMongsilStore: () -> Unit = {},
    onNavigateToThemeSetting: () -> Unit = {},
    onNavigateToFontStyle: () -> Unit = {},
    onNavigateToScreenLock: () -> Unit = {},
    onNavigateToBackupRestore: () -> Unit = {},
    onNavigateToPdfExport: () -> Unit = {},
    onNavigateToLanguageSetting: () -> Unit = {},
    onNavigateToAppReview: () -> Unit = {},
) {
    var isDiaryAlarmEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F5F5))
            .statusBarsPadding()
    ) {
        // 툴바 (뒤로 가기)
        SettingToolbar(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 섹션 1: 몽실 스토어
            SettingItem(
                icon = Res.drawable.ic_shopping_bag,
                label = "몽실 스토어",
                onClick = onNavigateToMongsilStore
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color(0xFFE0E0E0)
            )

            // 섹션 2: 알림, 테마, 글자
            SettingToggleItem(
                icon = Res.drawable.ic_notifications,
                label = "일기 알림",
                checked = isDiaryAlarmEnabled,
                onCheckedChange = { isDiaryAlarmEnabled = it }
            )

            SettingItem(
                icon = Res.drawable.ic_menu,
                label = "테마 설정",
                onClick = onNavigateToThemeSetting
            )

            SettingItem(
                icon = Res.drawable.ic_archive,
                label = "글자 스타일",
                onClick = onNavigateToFontStyle
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color(0xFFE0E0E0)
            )

            // 섹션 3: 보안, 백업, PDF, 언어
            SettingItem(
                icon = Res.drawable.ic_lock,
                label = "화면 잠금",
                onClick = onNavigateToScreenLock
            )

            SettingItem(
                icon = Res.drawable.ic_archive,
                label = "백업/복원",
                onClick = onNavigateToBackupRestore
            )

            SettingItem(
                icon = Res.drawable.ic_upload,
                label = "PDF 내보내기",
                onClick = onNavigateToPdfExport
            )

            SettingItem(
                icon = Res.drawable.ic_language,
                label = "언어 설정",
                onClick = onNavigateToLanguageSetting
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color(0xFFE0E0E0)
            )

            // 섹션 4: 앱 평가
            SettingItem(
                icon = Res.drawable.ic_menu,
                label = "앱 평가하기",
                onClick = onNavigateToAppReview
            )
        }
    }
}

@Composable
private fun SettingToolbar(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .clickable { onBack() },
            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
            contentDescription = "뒤로 가기",
            tint = Color.Black
        )
    }
}

@Composable
private fun SettingItem(
    icon: DrawableResource,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(icon),
            contentDescription = label,
            tint = Color.Black
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = label,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun SettingToggleItem(
    icon: DrawableResource,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(icon),
            contentDescription = label,
            tint = Color.Black
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = label,
            fontSize = 16.sp,
            color = Color.Black
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0),
                uncheckedBorderColor = Color.Transparent,
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.Black
            )
        )
    }
}
