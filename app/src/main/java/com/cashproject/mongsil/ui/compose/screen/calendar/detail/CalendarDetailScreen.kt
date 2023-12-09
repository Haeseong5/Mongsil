package com.cashproject.mongsil.ui.compose.screen.calendar.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cashproject.mongsil.R
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.compose.common.RoundedInputBox
import com.cashproject.mongsil.ui.compose.extensions.composableActivityViewModel
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.detail.DetailViewModel
import com.cashproject.mongsil.ui.theme.colorYellow
import com.cashproject.mongsil.ui.theme.dpToSp
import com.cashproject.mongsil.ui.theme.latoTextStyle
import com.cashproject.mongsil.util.PreferencesManager
import java.util.Date

@Composable
fun CalendarDetailScreen(
    viewModel: MainViewModel = composableActivityViewModel()
) {
    val comments = viewModel.commentEntityList.observeAsState()
    val poster = viewModel.allPosters.collectAsState()

    var url by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit, block = {
        url = poster.value.random().image
    })


    CalendarDetailScreenContent(
        comments = comments.value?.filter { it.date == Date() } ?: emptyList(),
        url = url,
        onUiAction = {
            when (it) {
                is CalendarDetailUiAction.OnConfirm -> {
                    viewModel.insertComment(
                        CommentEntity(
                            content = it.comment,
                            time = Date(),
                            emotion = PreferencesManager.selectedEmoticonId,
                            date = Date(),
                        )

                    )
                }
            }
        }
    )
}

sealed interface CalendarDetailUiAction {
    data class OnConfirm(val comment: String) : CalendarDetailUiAction

}

@Composable
private fun CalendarDetailScreenContent(
    comments: List<CommentEntity> = emptyList(),
    url: String = "",
    onUiAction: (CalendarDetailUiAction) -> Unit = {},
) {
    var inputComment by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = url,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        // TODO visible 옵션 추가
        Image(
            modifier = Modifier.noRippleClickable {
                // 백스택
            },
            painter = painterResource(id = R.drawable.emoticon_01_happy),
            contentDescription = "",

            )

        CommentInputBox(
            modifier = Modifier.align(Alignment.BottomCenter),
            inputComment = {
                inputComment = it
            },
            onClickEmoticon = {

            },
            onConfirm = {
                onUiAction.invoke(CalendarDetailUiAction.OnConfirm(inputComment))
            }
        )
        LazyColumn(content = {
            items(comments.size) {
                Text(text = comments[it].id.toString())
            }
        })
    }
}

@Composable
fun CommentInputBox(
    modifier: Modifier = Modifier,
    inputComment: (String) -> Unit = {},
    onClickEmoticon: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.noRippleClickable {
                onClickEmoticon.invoke()
            },
            painter = painterResource(id = R.drawable.emoticon_01_happy),
            contentDescription = "",
        )

        RoundedInputBox(
            modifier = Modifier.weight(1f),
            inputText = inputComment
        )
        Text(
            modifier = Modifier.noRippleClickable {
                onConfirm.invoke()
            },
            text = "확인",
            color = Color.White,
            fontSize = dpToSp(dp = 18.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SayingCommentList() {
    LazyColumn(content = {
        items(3) {

        }
    })
}

//@Composable
//fun SayingComment(
//    modifier: Modifier = Modifier,
//    comment: Comment
//) {
//    Row(modifier = modifier.padding(8.dp)) {
//        Image(
//            painter = painterResource(id = comment.url),
//            contentDescription = ""
//        )
//        Column(modifier = modifier.padding(start = 8.dp)) {
//            Text(
//                text = comment.time,
//                style = latoTextStyle,
//                fontSize = dpToSp(dp = 16.dp),
//                color = colorYellow
//            )
//
//            Text(
//                text = comment.text,
//                style = latoTextStyle,
//                fontSize = dpToSp(dp = 16.dp),
//                color = Color.White,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//
//}

@Preview
@Composable
private fun PreviewCalendarDetailScreen() {
    CalendarDetailScreenContent()
}