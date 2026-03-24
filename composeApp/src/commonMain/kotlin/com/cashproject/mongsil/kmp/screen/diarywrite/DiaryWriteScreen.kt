package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.LocalDarkTheme
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.EmoticonBottomSheet
import com.cashproject.mongsil.kmp.designsystem.component.EmoticonImage
import com.cashproject.mongsil.kmp.designsystem.component.IconToolbar
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.model.Emoticon
import com.cashproject.mongsil.kmp.model.ImageResource
import com.cashproject.mongsil.kmp.screen.diarywrite.component.BackgroundColorPalette
import com.cashproject.mongsil.kmp.screen.diarywrite.component.BottomToolbar
import com.cashproject.mongsil.kmp.screen.diarywrite.component.ColorPalette
import com.cashproject.mongsil.kmp.screen.diarywrite.component.DeleteConfirmDialog
import com.cashproject.mongsil.kmp.screen.diarywrite.component.PhotoPreviewScreen
import com.cashproject.mongsil.kmp.screen.diarywrite.component.ShowRewardedAd
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteEvent
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteSideEffect
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteUiState
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.day_of_week_friday
import mongsil.composeapp.generated.resources.day_of_week_monday
import mongsil.composeapp.generated.resources.day_of_week_saturday
import mongsil.composeapp.generated.resources.day_of_week_sunday
import mongsil.composeapp.generated.resources.day_of_week_thursday
import mongsil.composeapp.generated.resources.day_of_week_tuesday
import mongsil.composeapp.generated.resources.day_of_week_wednesday
import mongsil.composeapp.generated.resources.emoticon_01
import mongsil.composeapp.generated.resources.ic_baseline_arrow_back_ios_new_24
import mongsil.composeapp.generated.resources.ic_plus
import mongsil.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * 일기 작성 화면
 *
 * @param viewModel 일기 작성 ViewModel
 */
@Composable
fun DiaryWriteScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    onBack: () -> Unit,
    viewModel: DiaryWriteViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val openImagePicker = rememberImagePickerLauncher { imagePaths ->
        viewModel.onEvent(DiaryWriteEvent.OnPhotosSelected(imagePaths))
    }
    var rewardAdEmoticonId by remember { mutableStateOf<Int?>(null) }
    var isAdLoading by remember { mutableStateOf(false) }

    // SideEffect 처리
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                DiaryWriteSideEffect.DeleteSuccess -> onBack()
                DiaryWriteSideEffect.OnBack -> onBack()
                is DiaryWriteSideEffect.ShowRewardedAd -> {
                    rewardAdEmoticonId = effect.emoticonId
                    isAdLoading = true
                }
            }
        }
    }

    // 광고 로딩 중 다이얼로그
    if (isAdLoading) {
        AdLoadingDialog()
    }

    // 영상 광고 — rewardAdEmoticonId 가 있을 때만 컴포지션에 진입
    rewardAdEmoticonId?.let { emoticonId ->
        ShowRewardedAd(
            onRewarded = {
                isAdLoading = false
                viewModel.onEvent(DiaryWriteEvent.OnAdRewardEarned(emoticonId))
                rewardAdEmoticonId = null
            },
            onDismissed = {
                isAdLoading = false
                viewModel.onEvent(DiaryWriteEvent.OnAdDismissed)
                rewardAdEmoticonId = null
            },
        )
    }

    DiaryWriteScreenContent(
        modifier = modifier.padding(padding).consumeWindowInsets(padding),
        uiState = uiState,
        onEvent = viewModel::onEvent,
        openImagePicker = openImagePicker
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryWriteScreenContent(
    uiState: DiaryWriteUiState,
    onEvent: (DiaryWriteEvent) -> Unit,
    openImagePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackPressHandler(
        enabled = !uiState.showEmoticonBottomSheet && !uiState.showDeleteDialog && uiState.previewPhotoIndex == null
    ) {
        onEvent(DiaryWriteEvent.OnBackPressed)
    }

    val isDarkTheme = LocalDarkTheme.current
    val effectiveTextColor = remember(uiState.textColor, uiState.isTextColorCustomized, isDarkTheme) {
        when {
            uiState.isTextColorCustomized -> uiState.textColor
            isDarkTheme && uiState.textColor == Color.Black -> Color.White
            !isDarkTheme && uiState.textColor == Color.White -> Color.Black
            else -> uiState.textColor
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .statusBarsPadding()
            .imePadding()
    ) {
        AnimatedVisibility(
            visible = !uiState.isInitializing,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                IconToolbar(
                    modifier = Modifier.background(MongsilTheme.colorScheme.background),
                    leftContent = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_baseline_arrow_back_ios_new_24),
                            contentDescription = "뒤로 가기",
                            tint = MongsilTheme.colorScheme.labelStrong,
                            modifier = Modifier
                                .size(20.dp)
                                .circularRippleClickable { onEvent(DiaryWriteEvent.OnBackClick) }
                        )
                    },
                    rightContent = {
                        if (uiState.isExistingDiary) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_trash),
                                contentDescription = "삭제",
                                tint = MongsilTheme.colorScheme.fillRed,
                                modifier = Modifier
                                    .size(20.dp)
                                    .circularRippleClickable { onEvent(DiaryWriteEvent.OnDeleteClick) }
                            )
                        }
                    }
                )

                // 메인 콘텐츠
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .then(
                            if (uiState.backgroundColor != Color.Transparent) {
                                Modifier.background(uiState.backgroundColor)
                            } else Modifier
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // 이모티콘 버튼
                    EmoticonButton(
                        selectedEmoticon = uiState.selectedEmoticon,
                        onClick = { onEvent(DiaryWriteEvent.OnEmoticonButtonClick) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 날짜 표시
                    DateText(
                        year = uiState.year,
                        month = uiState.month,
                        day = uiState.day
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (uiState.photoUris.isNotEmpty()) {
                        SelectedPhotos(
                            photoUris = uiState.photoUris,
                            onRemove = { index ->
                                onEvent(DiaryWriteEvent.OnPhotoRemoved(index))
                            },
                            onPhotoClick = { index ->
                                onEvent(DiaryWriteEvent.OnPhotoClick(index))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // 일기 내용 입력
                    DiaryTextField(
                        content = uiState.content,
                        onContentChange = { onEvent(DiaryWriteEvent.OnContentChange(it)) },
                        enabled = !uiState.isLoading,
                        textAlign = uiState.textAlign,
                        textColor = effectiveTextColor,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    )
                }

                // 글자 색상 팔레트
                AnimatedVisibility(
                    visible = uiState.showColorPalette,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    ColorPalette(
                        selectedColor = uiState.textColor,
                        onColorSelected = { onEvent(DiaryWriteEvent.OnTextColorSelected(it)) },
                    )
                }

                // 배경 색상 팔레트
                AnimatedVisibility(
                    visible = uiState.showBackgroundColorPalette,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    BackgroundColorPalette(
                        selectedColor = uiState.backgroundColor,
                        onColorSelected = { onEvent(DiaryWriteEvent.OnBackgroundColorSelected(it)) },
                    )
                }

                // 하단 툴바
                BottomToolbar(
                    modifier = Modifier.background(MongsilTheme.colorScheme.card),
                    isSaving = uiState.isSaving,
                    canAddPhoto = uiState.canAddPhoto,
                    textAlign = uiState.textAlign,
                    textColor = effectiveTextColor,
                    backgroundColor = uiState.backgroundColor,
                    showColorPalette = uiState.showColorPalette,
                    showBackgroundColorPalette = uiState.showBackgroundColorPalette,
                    openImagePicker = openImagePicker,
                    onClickTime = { onEvent(DiaryWriteEvent.OnInsertCurrentTime) },
                    onTextAlignToggle = { onEvent(DiaryWriteEvent.OnTextAlignToggle) },
                    onColorPickerToggle = { onEvent(DiaryWriteEvent.OnColorPickerToggle) },
                    onBackgroundColorPickerToggle = { onEvent(DiaryWriteEvent.OnBackgroundColorPickerToggle) },
                )
            }
        } // AnimatedVisibility

        // 초기 로딩 오버레이
        AnimatedVisibility(
            visible = uiState.isInitializing,
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MongsilTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MongsilTheme.colorScheme.labelDisable.copy(alpha = 0.5f)
                )
            }
        }

        // 이모티콘 바텀시트
        if (uiState.showEmoticonBottomSheet) {
            EmoticonBottomSheet(
                emoticons = uiState.emoticons,
                unlockedPremiumIds = uiState.unlockedPremiumIds,
                onDismiss = { onEvent(DiaryWriteEvent.OnEmoticonBottomSheetDismiss) },
                onEmoticonSelected = { onEvent(DiaryWriteEvent.OnEmoticonSelected(it)) },
                onPremiumLocked = { onEvent(DiaryWriteEvent.OnPremiumEmoticonClick(it)) },
            )
        }

        // 삭제 확인 다이얼로그
        if (uiState.showDeleteDialog) {
            DeleteConfirmDialog(
                onConfirm = { onEvent(DiaryWriteEvent.OnDeleteConfirm) },
                onDismiss = { onEvent(DiaryWriteEvent.OnDeleteCancel) }
            )
        }

        // 전체화면 사진 미리보기
        uiState.previewPhotoIndex?.let { initialPage ->
            PhotoPreviewScreen(
                photoUris = uiState.photoUris,
                initialPage = initialPage,
                onDismiss = { onEvent(DiaryWriteEvent.OnPhotoPreviewDismiss) }
            )
        }
    }
}

@Composable
private fun SelectedPhotos(
    photoUris: List<String>,
    onRemove: (Int) -> Unit,
    onPhotoClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { photoUris.size })

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) { page ->
                AsyncImage(
                    model = photoUris[page],
                    contentDescription = "첨부 이미지",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onPhotoClick(page) },
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MongsilTheme.colorScheme.card.copy(alpha = 0.75f))
                    .clickable { onRemove(pagerState.currentPage) }
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "삭제",
                    style = MongsilTheme.typography.caption1,
                    color = MongsilTheme.colorScheme.labelStrong
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(photoUris.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) {
                                MongsilTheme.colorScheme.labelStrong
                            } else {
                                MongsilTheme.colorScheme.labelDisable
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun EmoticonButton(
    selectedEmoticon: Emoticon?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MongsilTheme.colorScheme.card)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selectedEmoticon != null) {
            EmoticonImage(
                image = selectedEmoticon.image,
                contentDescription = selectedEmoticon.title,
                modifier = Modifier.size(40.dp)
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.ic_plus),
                contentDescription = "back button",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun DateText(
    year: Int,
    month: Int,
    day: Int,
    modifier: Modifier = Modifier
) {
    val dayOfWeek = getDayOfWeekText(year, month, day)
    val dateText =
        "${year}.${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')} $dayOfWeek"

    Text(
        text = dateText,
        style = MongsilTheme.typography.default,
        modifier = modifier,
        color = MongsilTheme.colorScheme.labelStrong
    )
}

@Composable
private fun DiaryTextField(
    content: String,
    onContentChange: (String) -> Unit,
    enabled: Boolean,
    textAlign: TextAlign,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var textFieldValue by remember { mutableStateOf(TextFieldValue(content)) }

    // ViewModel에서 content가 외부에서 변경된 경우(시간 삽입 등) 커서를 끝으로 이동
    LaunchedEffect(content) {
        if (textFieldValue.text != content) {
            textFieldValue = TextFieldValue(
                text = content,
                selection = TextRange(content.length)
            )
        }
    }

    val scrollState = rememberScrollState()
    var height by remember { mutableStateOf(0) }
    var layoutResult: TextLayoutResult? by remember { mutableStateOf(null) }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onContentChange(newValue.text)
        },
        onTextLayout = { layoutResult = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .onSizeChanged { size ->
                coroutineScope.launch {
                    val result = layoutResult ?: return@launch
                    val cursorInView = textFieldValue.isCursorInView(
                        layoutResult = result,
                        height = size.height.toFloat(),
                        scrollValue = scrollState.value.toFloat()
                    )
                    if (!cursorInView && height > size.height) {
                        scrollState.scrollBy(
                            textFieldValue.calculateRequiredSizeScroll(
                                layoutResult = result,
                                oldHeight = height.toFloat(),
                                newHeight = size.height.toFloat(),
                                scrollValue = scrollState.value.toFloat()
                            )
                        )
                    }
                    height = size.height
                }
            },
        enabled = enabled,
        textStyle = MongsilTheme.typography.body1Medium.copy(
            textAlign = textAlign,
            color = textColor,
        ),
        cursorBrush = SolidColor(textColor),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                propagateMinConstraints = true
            ) {
                if (content.isEmpty()) {
                    Text(
                        text = "오늘 하루를 기록해보세요",
                        modifier = Modifier.fillMaxWidth(),
                        style = MongsilTheme.typography.default,
                        textAlign = textAlign,
                        color = MongsilTheme.colorScheme.labelWeak
                    )
                }
                innerTextField()
            }
        }
    )

    // 커서 위치 변경 시 스크롤
    LaunchedEffect(textFieldValue.selection) {
        val result = layoutResult ?: return@LaunchedEffect
        val cursorInView = textFieldValue.isCursorInView(
            layoutResult = result,
            height = height.toFloat(),
            scrollValue = scrollState.value.toFloat()
        )
        if (!cursorInView) {
            scrollState.scrollBy(
                textFieldValue.calculateRequiredSelectionScroll(
                    layoutResult = result,
                    height = height.toFloat(),
                    scrollValue = scrollState.value.toFloat()
                )
            )
        }
    }
}


@Composable
private fun AdLoadingDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MongsilTheme.colorScheme.card),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    color = MongsilTheme.colorScheme.labelStrong,
                    modifier = Modifier.size(36.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "광고 로딩 중",
                    style = MongsilTheme.typography.caption1,
                    color = MongsilTheme.colorScheme.labelWeak,
                )
            }
        }
    }
}

// 요일 텍스트 반환
@Composable
private fun getDayOfWeekText(year: Int, month: Int, day: Int): String {
    return when (LocalDate(year, month, day).dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(Res.string.day_of_week_monday)
        DayOfWeek.TUESDAY -> stringResource(Res.string.day_of_week_tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.day_of_week_wednesday)
        DayOfWeek.THURSDAY -> stringResource(Res.string.day_of_week_thursday)
        DayOfWeek.FRIDAY -> stringResource(Res.string.day_of_week_friday)
        DayOfWeek.SATURDAY -> stringResource(Res.string.day_of_week_saturday)
        DayOfWeek.SUNDAY -> stringResource(Res.string.day_of_week_sunday)
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenContentPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026,
                month = 2,
                day = 4,
                content = "오늘은 날씨가 정말 좋았다.\n아침부터 햇살이 따스하게 비춰서 기분이 좋았다.",
                selectedEmoticon = null,
                emoticons = emptyList(),
                showEmoticonBottomSheet = false,
                isLoading = false,
                isInitializing = false
            ),
            onEvent = {},
            openImagePicker = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenContentEmptyPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026,
                month = 2,
                day = 4,
                content = "",
                selectedEmoticon = null,
                emoticons = emptyList(),
                showEmoticonBottomSheet = false,
                isLoading = false,
                isInitializing = false
            ),
            onEvent = {},
            openImagePicker = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteScreenContentWithEmoticonPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026,
                month = 2,
                day = 4,
                content = "",
                selectedEmoticon = Emoticon(
                    id = 1,
                    title = "행복",
                    image = ImageResource.Local(Res.drawable.emoticon_01),
                    textColor = "#333333",
                    backgroundColor = "#FFE5E5"
                ),
                emoticons = emptyList(),
                showEmoticonBottomSheet = false,
                isLoading = false,
                isInitializing = false
            ),
            onEvent = {},
            openImagePicker = {}
        )
    }
}

private val previewContent = "오늘은 날씨가 정말 좋았다.\n아침부터 햇살이 따스하게 비춰서 기분이 좋았다."

@Preview(showBackground = true)
@Composable
private fun DiaryWriteAlignStartPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026, month = 3, day = 14,
                content = previewContent,
                isInitializing = false,
                textAlign = TextAlign.Start
            ),
            onEvent = {},
            openImagePicker = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteAlignCenterPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026, month = 3, day = 14,
                content = previewContent,
                isInitializing = false,
                textAlign = TextAlign.Center
            ),
            onEvent = {},
            openImagePicker = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryWriteAlignEndPreview() {
    MongsilTheme {
        DiaryWriteScreenContent(
            uiState = DiaryWriteUiState(
                year = 2026, month = 3, day = 14,
                content = previewContent,
                isInitializing = false,
                textAlign = TextAlign.End
            ),
            onEvent = {},
            openImagePicker = {}
        )
    }
}

private fun TextFieldValue.isCursorInView(
    layoutResult: TextLayoutResult,
    height: Float,
    scrollValue: Float,
): Boolean = with(layoutResult) {
    val currentLine =
        getLineForOffset(selection.min.coerceIn(0, layoutResult.layoutInput.text.length))
    val lineBottom = getLineBottom(currentLine)
    val lineTop = getLineTop(currentLine)
    lineBottom <= height + scrollValue && lineTop >= scrollValue
}

private fun TextFieldValue.calculateRequiredSelectionScroll(
    layoutResult: TextLayoutResult,
    height: Float,
    scrollValue: Float,
): Float = with(layoutResult) {
    val currentLine =
        getLineForOffset(selection.min.coerceIn(0, layoutResult.layoutInput.text.length))
    val lineTop = getLineTop(currentLine)
    val lineBottom = getLineBottom(currentLine)
    if (lineTop < scrollValue) -(scrollValue - lineTop)
    else if (lineBottom > height + scrollValue) lineBottom - (height + scrollValue)
    else 0f
}

private fun TextFieldValue.calculateRequiredSizeScroll(
    layoutResult: TextLayoutResult,
    oldHeight: Float,
    newHeight: Float,
    scrollValue: Float,
): Float = with(layoutResult) {
    val currentLine =
        getLineForOffset(selection.min.coerceIn(0, layoutResult.layoutInput.text.length))
    val lineBottom = getLineBottom(currentLine)
    val sizeDifference = oldHeight - newHeight
    if (lineBottom in (newHeight + scrollValue)..(oldHeight + scrollValue))
        sizeDifference - (oldHeight - (lineBottom - scrollValue))
    else 0f
}
