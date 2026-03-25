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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.LocalDarkTheme
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.ObserveErrorEffect
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.backup_restore_title
import mongsil.composeapp.generated.resources.cd_navigate_back
import mongsil.composeapp.generated.resources.ic_archive
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_menu
import mongsil.composeapp.generated.resources.ic_notifications
import mongsil.composeapp.generated.resources.setting_app_review
import mongsil.composeapp.generated.resources.setting_diary_alarm
import mongsil.composeapp.generated.resources.setting_font_style
import mongsil.composeapp.generated.resources.setting_theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

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
    viewModel: SettingViewModel = koinViewModel(),
) {
    val isDiaryAlarmEnabled by viewModel.isDiaryReminderEnabled.collectAsStateWithLifecycle()

    ObserveErrorEffect(viewModel.errorEvent)

    val permissionRequester = rememberDiaryReminderPermissionRequester(
        onPermissionResult = { granted ->
            viewModel.updateDiaryReminder(granted)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MongsilTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // 툴바 (뒤로 가기)
        CommonToolbar(onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // TODO 미개발 기능
            // 섹션 1: 몽실 스토어
//            SettingItem(
//                icon = Res.drawable.ic_shopping_bag,
//                label = "몽실 스토어",
//                onClick = { viewModel.logMenuClick("store"); onNavigateToMongsilStore() }
//            )

//            HorizontalDivider(
//                modifier = Modifier.padding(horizontal = 20.dp),
//                color = Color(0xFFE0E0E0)
//            )

            // 섹션 2: 알림, 테마, 글자
            SettingToggleItem(
                icon = Res.drawable.ic_notifications,
                label = stringResource(Res.string.setting_diary_alarm),
                checked = isDiaryAlarmEnabled,
                onCheckedChange = { enabled ->
                    if (enabled) permissionRequester() else viewModel.updateDiaryReminder(false)
                }
            )

            SettingItem(
                icon = Res.drawable.ic_menu,
                label = stringResource(Res.string.setting_theme),
                onClick = { viewModel.logMenuClick("theme"); onNavigateToThemeSetting() }
            )

            SettingItem(
                icon = Res.drawable.ic_archive,
                label = stringResource(Res.string.setting_font_style),
                onClick = { viewModel.logMenuClick("font_style"); onNavigateToFontStyle() }
            )

//            HorizontalDivider(
//                modifier = Modifier.padding(horizontal = 20.dp),
//                color = Color(0xFFE0E0E0)
//            )

            // TODO 다음 배포에 추가
            // 섹션 3: 보안, 백업, PDF, 언어
//            SettingItem(
//                icon = Res.drawable.ic_lock,
//                label = "화면 잠금",
//                onClick = { viewModel.logMenuClick("screen_lock"); onNavigateToScreenLock() }
//            )

            SettingItem(
                icon = Res.drawable.ic_archive,
                label = stringResource(Res.string.backup_restore_title),
                onClick = { viewModel.logMenuClick("backup_restore"); onNavigateToBackupRestore() }
            )

            // TODO 다음 배포에 추가
//            SettingItem(
//                icon = Res.drawable.ic_upload,
//                label = "PDF 내보내기",
//                onClick = { viewModel.logMenuClick("pdf_export"); onNavigateToPdfExport() }
//            )

            // TODO 미개발 기능
//            SettingItem(
//                icon = Res.drawable.ic_language,
//                label = "언어 설정",
//                onClick = { viewModel.logMenuClick("language"); onNavigateToLanguageSetting() }
//            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color(0xFFE0E0E0)
            )

            // 섹션 4: 앱 평가
            SettingItem(
                icon = Res.drawable.ic_menu,
                label = stringResource(Res.string.setting_app_review),
                onClick = { viewModel.logMenuClick("app_review"); onNavigateToAppReview() }
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
            contentDescription = stringResource(Res.string.cd_navigate_back),
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
            tint = MongsilTheme.colorScheme.labelStrong
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = label,
            style = MongsilTheme.typography.default,
            color = MongsilTheme.colorScheme.labelStrong
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
    val isDark = LocalDarkTheme.current
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
            tint = MongsilTheme.colorScheme.labelStrong
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = label,
            style = MongsilTheme.typography.default,
            color = MongsilTheme.colorScheme.labelStrong
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Gray300,
                uncheckedBorderColor = Color.Transparent,
                checkedThumbColor = if (isDark) Color.Black else Color.White,
                checkedTrackColor = MongsilTheme.colorScheme.labelStrong
            )
        )
    }
}
