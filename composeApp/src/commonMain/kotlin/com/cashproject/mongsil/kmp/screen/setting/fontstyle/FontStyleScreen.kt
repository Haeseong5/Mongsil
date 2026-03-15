package com.cashproject.mongsil.kmp.screen.setting.fontstyle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.cashproject.mongsil.kmp.model.FontStyleOption
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.component.FontOptionItem
import com.cashproject.mongsil.kmp.screen.setting.fontstyle.component.TextSizeControl
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.emoticon_04
import org.jetbrains.compose.resources.painterResource
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
            title = "글자 스타일"
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
                contentDescription = "미리보기 이모티콘"
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = getTodayText(),
                style = MongsilTheme.typography.body1Normal,
                color = MongsilTheme.colorScheme.labelWeak
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = "꼬박꼬박 일기 쓰는 습관 :)\n폰트 사이즈를 변경할 수 있어요",
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
private fun getTodayText(): String {
    val today = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dayOfWeek = when (today.dayOfWeek) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }

    val month = today.monthNumber.toString().padStart(2, '0')
    val day = today.dayOfMonth.toString().padStart(2, '0')
    return "${today.year}.${month}.${day} $dayOfWeek"
}

private val fontOptions: List<Pair<FontStyleOption, String>> = listOf(
    FontStyleOption.SYSTEM to "시스템 폰트",
    FontStyleOption.GAMJA_FLOWER to "감자꽃체",
)

@Preview
@Composable
private fun FontStyleScreenPreview() {
    MongsilTheme {
        FontStyleScreen(
            onBack = {}
        )
    }
}
