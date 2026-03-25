package com.cashproject.mongsil.kmp.screen.diarychart

import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.screen.diarychart.model.WordCloudItem

class GetWordCloudUseCase(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
        minCount: Int = 2,
    ): List<WordCloudItem> {
        val wordCounts = diaryRepository.getDiariesByYearMonth(year, month)
            .flatMap { diary ->
                normalizeContractions(diary.content)
                    .split(Regex("[\\s\\p{Punct}.,!?…\\-·。、]+"))
                    .map { it.lowercase() }
                    .filter { token ->
                        token.length >= 2
                                && token !in STOP_WORDS
                                && !token.all { it.isDigit() }
                    }
            }
            .groupingBy { it }
            .eachCount()

        return wordCounts.entries
            .filter { it.value >= minCount }
            .sortedByDescending { it.value }
            .take(60)
            .map { (word, count) -> WordCloudItem(word = word, count = count) }
    }

    private fun normalizeContractions(text: String): String {
        return text
            .replace(Regex("(?i)n't"), " not")
            .replace(Regex("(?i)'m"), " am")
            .replace(Regex("(?i)'re"), " are")
            .replace(Regex("(?i)'ve"), " have")
            .replace(Regex("(?i)'ll"), " will")
            .replace(Regex("(?i)'d"), " would")
            .replace(Regex("(?i)'s"), "")
    }

    companion object {
        private val STOP_WORDS = setOf(
            // 한국어
            "이", "가", "은", "는", "을", "를", "의", "에", "도", "에서", "으로", "로", "과", "와",
            "이나", "나", "이라", "라", "이고", "고", "하고", "에게", "한테", "보다", "만큼", "처럼",
            "같이", "까지", "부터", "이랑", "랑", "하며", "며", "이며", "했", "한", "하는", "이다",
            "그", "것", "수", "때", "더", "같은", "이런", "저런", "그런", "어떤", "오늘", "일",
            "나는", "내가", "그리고", "그래서", "하지만", "그런데", "아", "그냥", "진짜", "너무",
            "정말", "매우", "아주", "좀", "이제", "여기", "저기", "거기", "우리", "저는", "제가",
            "나도", "있다", "없다", "하다", "되다", "되어", "됩니다", "않고", "않는", "않아",
            "있어", "없어", "했어", "한다", "합니다", "이에", "에도", "에서도", "으로도",
            // 영어
            "the", "be", "to", "of", "and", "in", "that", "have", "it", "for",
            "not", "on", "with", "he", "as", "you", "do", "at", "this", "but",
            "his", "by", "from", "they", "we", "say", "her", "she", "or", "an",
            "will", "my", "one", "all", "would", "there", "their", "what", "so",
            "up", "out", "if", "about", "who", "get", "which", "go", "me", "when",
            "make", "can", "like", "time", "no", "just", "him", "know", "take",
            "people", "into", "year", "your", "good", "some", "could", "them",
            "see", "other", "than", "then", "now", "look", "only", "come", "its",
            "over", "think", "also", "back", "after", "use", "two", "how", "our",
            "work", "first", "well", "way", "even", "new", "want", "because",
            "any", "these", "give", "day", "most", "us", "is", "am", "are", "was",
            "were", "been", "being", "has", "had", "did", "does", "doing", "very",
            "really", "too", "here", "much", "many", "such", "own", "should",
            // 추가: 축약형 정규화 후 남는 단어 + 일기 고빈도 불용어
            "not", "got", "went", "going", "made",
            "today", "yesterday", "tomorrow", "tonight",
            "thing", "things", "something", "anything", "nothing",
            "feel", "felt", "feeling", "still", "maybe",
        )
    }
}