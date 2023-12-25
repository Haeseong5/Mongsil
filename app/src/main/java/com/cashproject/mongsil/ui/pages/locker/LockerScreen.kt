package com.cashproject.mongsil.ui.pages.locker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.component.Toolbar

@Composable
fun LockerScreen(
    uiState: LockerUiState,
    onSetting: () -> Unit,
    onPoster: (Poster) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = getStatusBarHeight())
    ) {
        Toolbar(
            modifier = Modifier,
            title = "관심",
            isVisibleSettingButton = true,
            visibleBack = false,
            onEndButtonClick = onSetting
        )
        LockerScreenContent(
            uiState = uiState,
            onPoster = onPoster
        )
    }
}

@Composable
fun LockerScreenContent(uiState: LockerUiState, onPoster: (Poster) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        content = {
            items(uiState.posters + uiState.posters + uiState.posters + uiState.posters) {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .noRippleClickable {
                            onPoster.invoke(it)
                        },
                    model = it.squareImage,
                    contentDescription = null
                )
            }
        }
    )
}