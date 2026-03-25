package com.cashproject.mongsil.kmp.screen.setting.fontstyle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.designsystem.extensions.fixedScaleTextStyle
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.component.FontOptionItem
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.component.TextSizeControl
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.cd_preview_emoticon
import mongsil.composeapp.generated.resources.day_of_week_friday
import mongsil.composeapp.generated.resources.day_of_week_monday
import mongsil.composeapp.generated.resources.day_of_week_saturday
import mongsil.composeapp.generated.resources.day_of_week_sunday
import mongsil.composeapp.generated.resources.day_of_week_thursday
import mongsil.composeapp.generated.resources.day_of_week_tuesday
import mongsil.composeapp.generated.resources.day_of_week_wednesday
import mongsil.composeapp.generated.resources.emoticon_04
import mongsil.composeapp.generated.resources.font_gamja_flower
import mongsil.composeapp.generated.resources.font_preview_text
import mongsil.composeapp.generated.resources.font_system
import mongsil.composeapp.generated.resources.setting_font_style
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@Composable
fun FontStyleScreen(
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    viewModel: FontStyleViewModel = koinViewModel(),
) {
    val selectedFontStyle by viewModel.selectedFontStyle.collectAsStateWithLifecycle()
    val fontScale by viewModel.fontScale.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding)
    ) {
        CommonToolbar(
            onBack = onBack,
            title = stringResource(Res.string.setting_font_style)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FontPreviewCard()

            TextSizeControl(
                fontScale = fontScale,
                onScaleChange = viewModel::updateFontScale
            )

            FontSelectorCard(
                selectedFontStyle = selectedFontStyle,
                onSelect = viewModel::updateFontStyle
            )
        }
    }
}

@Composable
private fun FontPreviewCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MongsilTheme.colorScheme.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(84.dp),
                painter = painterResource(Res.drawable.emoticon_04),
                contentDescription = stringResource(Res.string.cd_preview_emoticon)
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = getTodayText(),
                style = fixedScaleTextStyle(MongsilTheme.typography.body2Normal),
                color = MongsilTheme.colorScheme.labelWeak
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(top = 16.dp),
                text = stringResource(Res.string.font_preview_text),
                style = MongsilTheme.typography.heading2,
                color = MongsilTheme.colorScheme.labelStrong,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun FontSelectorCard(
    selectedFontStyle: FontStyleOption,
    onSelect: (FontStyleOption) -> Unit,
) {
    val fontOptions = listOf(
        FontStyleOption.SYSTEM to stringResource(Res.string.font_system),
        FontStyleOption.GAMJA_FLOWER to stringResource(Res.string.font_gamja_flower),
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MongsilTheme.colorScheme.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            fontOptions.forEach { (option, label) ->
                FontOptionItem(
                    label = label,
                    selected = selectedFontStyle == option,
                    onClick = { onSelect(option) },
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun getTodayText(): String {
    val today = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dayOfWeek = when (today.dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(Res.string.day_of_week_monday)
        DayOfWeek.TUESDAY -> stringResource(Res.string.day_of_week_tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.day_of_week_wednesday)
        DayOfWeek.THURSDAY -> stringResource(Res.string.day_of_week_thursday)
        DayOfWeek.FRIDAY -> stringResource(Res.string.day_of_week_friday)
        DayOfWeek.SATURDAY -> stringResource(Res.string.day_of_week_saturday)
        DayOfWeek.SUNDAY -> stringResource(Res.string.day_of_week_sunday)
    }
    val month = today.monthNumber.toString().padStart(2, '0')
    val day = today.dayOfMonth.toString().padStart(2, '0')
    return "${today.year}.${month}.${day} $dayOfWeek"
}

@Preview
@Composable
private fun FontStyleScreenPreview() {
    MongsilTheme {
        FontStyleScreen(
            onBack = {}
        )
    }
}
