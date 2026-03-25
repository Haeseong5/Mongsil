package com.cashproject.mongsil.kmp.screen.setting.pdfexport

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.model.PdfExportUiState
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.pdf_create
import mongsil.composeapp.generated.resources.pdf_export_description
import mongsil.composeapp.generated.resources.setting_pdf_export
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PdfExportScreen(
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    viewModel: PdfExportViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PdfExportScreenContent(
        uiState = uiState,
        onBack = onBack,
        onExportClick = viewModel::exportPdf,
        padding = padding,
    )
}

@Composable
private fun PdfExportScreenContent(
    uiState: PdfExportUiState,
    onBack: () -> Unit,
    onExportClick: () -> Unit,
    padding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding)
    ) {
        CommonToolbar(
            onBack = onBack,
            title = stringResource(Res.string.setting_pdf_export)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.pdf_export_description),
                style = MongsilTheme.typography.body2Medium,
                color = MongsilTheme.colorScheme.labelWeak,
                textAlign = TextAlign.Center
            )

            ExportButton(
                progress = uiState.progress,
                isExporting = uiState.isExporting,
                label = if (uiState.isExporting) {
                    "${(uiState.progress * 100).toInt()}%  ${uiState.progressMessage}"
                } else {
                    stringResource(Res.string.pdf_create)
                },
                onClick = onExportClick
            )

            uiState.exportedFile?.let { file ->
                StatusText(
                    text = "${file.name}\n${file.path}",
                    color = MongsilTheme.colorScheme.labelWeak
                )
            }

            uiState.errorMessage?.let { message ->
                StatusText(
                    text = message,
                    color = MongsilTheme.colorScheme.fillRed
                )
            }
        }
    }
}

@Composable
private fun ExportButton(
    progress: Float,
    isExporting: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .padding(top = 28.dp)
            .widthIn(min = 240.dp, max = 320.dp)
            .height(60.dp)
            .clip(shape)
            .background(MongsilTheme.colorScheme.labelStrong)
            .border(1.dp, MongsilTheme.colorScheme.labelStrong, shape)
            .clickable(enabled = !isExporting, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isExporting) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = MongsilTheme.colorScheme.fillBlue,
                trackColor = MongsilTheme.colorScheme.labelStrong,
            )
        }

        Text(
            text = label,
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.background,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatusText(
    text: String,
    color: androidx.compose.ui.graphics.Color,
) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = text,
        style = MongsilTheme.typography.caption1,
        color = color,
        textAlign = TextAlign.Center
    )
}
