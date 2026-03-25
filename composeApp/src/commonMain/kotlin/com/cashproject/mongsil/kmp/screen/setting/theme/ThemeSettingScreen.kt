package com.cashproject.mongsil.kmp.screen.setting.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.model.ThemeMode
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.setting_theme
import mongsil.composeapp.generated.resources.theme_dark
import mongsil.composeapp.generated.resources.theme_light
import mongsil.composeapp.generated.resources.theme_system
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThemeSettingScreen(
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    viewModel: ThemeSettingViewModel = koinViewModel(),
) {
    val selectedMode by viewModel.selectedMode.collectAsStateWithLifecycle()

    ThemeSettingScreenContent(
        selectedMode = selectedMode,
        onModeSelected = viewModel::updateThemeMode,
        onBack = onBack,
        padding = padding,
    )
}

@Composable
private fun ThemeSettingScreenContent(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    onBack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding)
    ) {
        CommonToolbar(
            onBack = onBack,
            title = stringResource(Res.string.setting_theme)
        )

        ThemeModeItem(
            label = stringResource(Res.string.theme_system),
            selected = selectedMode == ThemeMode.SYSTEM,
            onClick = { onModeSelected(ThemeMode.SYSTEM) }
        )

        ThemeModeItem(
            label = stringResource(Res.string.theme_light),
            selected = selectedMode == ThemeMode.LIGHT,
            onClick = { onModeSelected(ThemeMode.LIGHT) }
        )

        ThemeModeItem(
            label = stringResource(Res.string.theme_dark),
            selected = selectedMode == ThemeMode.DARK,
            onClick = { onModeSelected(ThemeMode.DARK) }
        )
    }
}

@Composable
private fun ThemeModeItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MongsilTheme.typography.default,
            color = MongsilTheme.colorScheme.labelStrong
        )
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MongsilTheme.colorScheme.labelStrong,
                unselectedColor = Gray300
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ThemeSettingSystemSelectedPreview() {
    MongsilTheme {
        ThemeSettingScreenContent(
            selectedMode = ThemeMode.SYSTEM,
            onModeSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSettingLightSelectedPreview() {
    MongsilTheme(darkTheme = true) {
        ThemeSettingScreenContent(
            selectedMode = ThemeMode.LIGHT,
            onModeSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSettingDarkSelectedPreview() {
    MongsilTheme(darkTheme = false) {
        ThemeSettingScreenContent(
            selectedMode = ThemeMode.DARK,
            onModeSelected = {}
        )
    }
}

// endregion
