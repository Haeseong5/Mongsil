package com.cashproject.mongsil.kmp.core.data

import com.cashproject.mongsil.kmp.core.model.TextSource
import com.cashproject.mongsil.kmp.model.Emoticon
import com.cashproject.mongsil.kmp.model.ImageResource
import com.cashproject.mongsil.kmp.network.model.EmoticonResponse
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.emoticon_01
import mongsil.composeapp.generated.resources.emoticon_02
import mongsil.composeapp.generated.resources.emoticon_03
import mongsil.composeapp.generated.resources.emoticon_04
import mongsil.composeapp.generated.resources.emoticon_05
import mongsil.composeapp.generated.resources.emoticon_06
import mongsil.composeapp.generated.resources.emoticon_07
import mongsil.composeapp.generated.resources.emoticon_08
import mongsil.composeapp.generated.resources.emoticon_09
import mongsil.composeapp.generated.resources.emoticon_10
import mongsil.composeapp.generated.resources.emoticon_11
import mongsil.composeapp.generated.resources.emoticon_12
import mongsil.composeapp.generated.resources.emoticon_13
import mongsil.composeapp.generated.resources.emoticon_14
import mongsil.composeapp.generated.resources.emoticon_15
import mongsil.composeapp.generated.resources.emoticon_title_angry
import mongsil.composeapp.generated.resources.emoticon_title_anxious
import mongsil.composeapp.generated.resources.emoticon_title_bored
import mongsil.composeapp.generated.resources.emoticon_title_depressed
import mongsil.composeapp.generated.resources.emoticon_title_disappointed
import mongsil.composeapp.generated.resources.emoticon_title_disgusted
import mongsil.composeapp.generated.resources.emoticon_title_embarrassed
import mongsil.composeapp.generated.resources.emoticon_title_happy
import mongsil.composeapp.generated.resources.emoticon_title_joy
import mongsil.composeapp.generated.resources.emoticon_title_lonely
import mongsil.composeapp.generated.resources.emoticon_title_normal
import mongsil.composeapp.generated.resources.emoticon_title_sad
import mongsil.composeapp.generated.resources.emoticon_title_satisfied
import mongsil.composeapp.generated.resources.emoticon_title_surprised
import mongsil.composeapp.generated.resources.emoticon_title_tired

private const val COMPOSE_RESOURCES_BASE =
    "composeResources/mongsil.composeapp.generated.resources/drawable/"

class EmoticonRepository() {

    fun getEmoticons(): List<Emoticon> = listOf(
        Emoticon(
            id = 0,
            title = TextSource.Res(Res.string.emoticon_title_happy),
            image = localImage(1, Res.drawable.emoticon_01),
            textColor = "#dcc75a",
            backgroundColor = "#fff9da"
        ),
        Emoticon(
            id = 1,
            title = TextSource.Res(Res.string.emoticon_title_joy),
            image = localImage(2, Res.drawable.emoticon_02),
            textColor = "#db8fbd",
            backgroundColor = "#ffecf7"
        ),
        Emoticon(
            id = 2,
            title = TextSource.Res(Res.string.emoticon_title_satisfied),
            image = localImage(3, Res.drawable.emoticon_03),
            textColor = "#dc8d6c",
            backgroundColor = "#ffe4d9"
        ),
        Emoticon(
            id = 3,
            title = TextSource.Res(Res.string.emoticon_title_normal),
            image = localImage(4, Res.drawable.emoticon_04),
            textColor = "#d78787",
            backgroundColor = "#ffe8e8"
        ),
        Emoticon(
            id = 4,
            title = TextSource.Res(Res.string.emoticon_title_tired),
            image = localImage(5, Res.drawable.emoticon_05),
            textColor = "#9197d2",
            backgroundColor = "#e5e8ff"
        ),
        Emoticon(
            id = 5,
            title = TextSource.Res(Res.string.emoticon_title_embarrassed),
            image = localImage(6, Res.drawable.emoticon_06),
            textColor = "#d9ad58",
            backgroundColor = "#ffecc9"
        ),
        Emoticon(
            id = 6,
            title = TextSource.Res(Res.string.emoticon_title_bored),
            image = localImage(7, Res.drawable.emoticon_07),
            textColor = "#527e57",
            backgroundColor = "#cce5ce"
        ),
        Emoticon(
            id = 7,
            title = TextSource.Res(Res.string.emoticon_title_angry),
            image = localImage(8, Res.drawable.emoticon_08),
            textColor = "#cd5454",
            backgroundColor = "#ffdbdb"
        ),
        Emoticon(
            id = 8,
            title = TextSource.Res(Res.string.emoticon_title_disgusted),
            image = localImage(9, Res.drawable.emoticon_09),
            textColor = "#9e9896",
            backgroundColor = "#efebea"
        ),
        Emoticon(
            id = 9,
            title = TextSource.Res(Res.string.emoticon_title_disappointed),
            image = localImage(10, Res.drawable.emoticon_10),
            textColor = "#d3d5e3",
            backgroundColor = "#454d7a"
        ),
        Emoticon(
            id = 10,
            title = TextSource.Res(Res.string.emoticon_title_anxious),
            image = localImage(11, Res.drawable.emoticon_11),
            textColor = "#555454",
            backgroundColor = "#dddddd"
        ),
        Emoticon(
            id = 11,
            title = TextSource.Res(Res.string.emoticon_title_depressed),
            image = localImage(12, Res.drawable.emoticon_12),
            textColor = "#8f6b5b",
            backgroundColor = "#f0dad1"
        ),
        Emoticon(
            id = 12,
            title = TextSource.Res(Res.string.emoticon_title_sad),
            image = localImage(13, Res.drawable.emoticon_13),
            textColor = "#465761",
            backgroundColor = "#ccdbe0"
        ),
        Emoticon(
            id = 13,
            title = TextSource.Res(Res.string.emoticon_title_surprised),
            image = localImage(14, Res.drawable.emoticon_14),
            textColor = "#889456",
            backgroundColor = "#ecf1da"
        ),
        Emoticon(
            id = 14,
            title = TextSource.Res(Res.string.emoticon_title_lonely),
            image = localImage(15, Res.drawable.emoticon_15),
            textColor = "#7b9a9b",
            backgroundColor = "#ddebec"
        ),
    )
}

/** 프리미엄 이모티콘 ID 목록 — 영상 광고 시청 후 잠금 해제 */
val PREMIUM_EMOTICON_IDS: Set<Int> = setOf(9, 12, 14)

private fun emoticonIdToImageResource(id: Int): ImageResource = when (id) {
    0 -> localImage(1, Res.drawable.emoticon_01)
    1 -> localImage(2, Res.drawable.emoticon_02)
    2 -> localImage(3, Res.drawable.emoticon_03)
    3 -> localImage(4, Res.drawable.emoticon_04)
    4 -> localImage(5, Res.drawable.emoticon_05)
    5 -> localImage(6, Res.drawable.emoticon_06)
    6 -> localImage(7, Res.drawable.emoticon_07)
    7 -> localImage(8, Res.drawable.emoticon_08)
    8 -> localImage(9, Res.drawable.emoticon_09)
    9 -> localImage(10, Res.drawable.emoticon_10)
    10 -> localImage(11, Res.drawable.emoticon_11)
    11 -> localImage(12, Res.drawable.emoticon_12)
    12 -> localImage(13, Res.drawable.emoticon_13)
    13 -> localImage(14, Res.drawable.emoticon_14)
    14 -> localImage(15, Res.drawable.emoticon_15)
    else -> localImage(1, Res.drawable.emoticon_01)
}

/**
 * DrawableResource의 내부 프로퍼티(id, items)는 모두 internal이라 모듈 외부에서 접근 불가.
 * assetPath를 직접 제공하여 비-Compose 컨텍스트(PDF, 위젯)에서도 로드 가능하게 합니다.
 */
private fun localImage(index: Int, resource: org.jetbrains.compose.resources.DrawableResource) =
    ImageResource.Local(
        resource = resource,
        assetPath = "${COMPOSE_RESOURCES_BASE}emoticon_${index.toString().padStart(2, '0')}.png",
    )

/**
 * EmoticonResponse를 도메인 모델(Emoticon)로 변환
 * 이미지는 서버 URL 대신 ID 기반 로컬 리소스를 사용합니다.
 */
private fun EmoticonResponse.toEmoticon() = Emoticon(
    id = id,
    title = TextSource.Value(title),
    image = emoticonIdToImageResource(id),
    textColor = textColor,
    backgroundColor = backgroundColor,
    isPremium = id in PREMIUM_EMOTICON_IDS,
)
