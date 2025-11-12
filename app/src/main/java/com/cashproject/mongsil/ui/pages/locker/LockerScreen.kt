package com.cashproject.mongsil.ui.pages.locker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.component.Toolbar
import com.cashproject.mongsil.ui.pages.diary.model.Poster

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
            actionIcon = Icons.Outlined.Settings,
            actionIconClicked = onSetting,
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
            items(uiState.posters) {
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