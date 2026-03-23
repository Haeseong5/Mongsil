package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme


@Composable
fun DayPickerDialog(
    initialYear: Int,
    onDismissRequest: () -> Unit,
    onMonthSelected: (year: Int, month: Int) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        DayPickerContent(
            initialYear = initialYear,
            onMonthSelected = onMonthSelected
        )
    }
}

@Composable
private fun DayPickerContent(
    initialYear: Int,
    onMonthSelected: (year: Int, month: Int) -> Unit
) {
    var selectedYear by remember { mutableStateOf(initialYear) }

    Column(
        modifier = Modifier
            .background(MongsilTheme.colorScheme.alert, RoundedCornerShape(20.dp))
            .padding(24.dp)
    ) {
        // 년도 선택 헤더
        YearHeader(
            year = selectedYear,
            goToPrevious = { selectedYear-- },
            goToNext = { selectedYear++ }
        )

        // 월 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 32.dp)
        ) {
            items((1..12).toList()) { month ->
                MonthItem(
                    month = month,
                    onClick = { onMonthSelected(selectedYear, month) }
                )
            }
        }
    }
}

@Composable
private fun MonthItem(
    month: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .size(50.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = month.toString(),
            style = MongsilTheme.typography.body1Normal,
            textAlign = TextAlign.Center,
            color = MongsilTheme.colorScheme.labelStrong
        )
    }
}

@Preview
@Composable
private fun DayPickerContentPreview() {
    DayPickerContent(
        initialYear = 2025,
        onMonthSelected = { _, _ -> }
    )
}