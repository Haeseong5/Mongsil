package com.cashproject.mongsil.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.model.Emoticons
import com.cashproject.mongsil.ui.model.defaultEmoticon
import java.util.*

@Entity(tableName = "Comment")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val content: String,
    @ColumnInfo
    val emotion: Int,
    @ColumnInfo("time")
    val writeTime: Date,
    @ColumnInfo
    val date: Date, //어떤 날짜에 작성했는 지. 시간 고려 X
    @Deprecated("사용되지 않는 필드입니다. 추후 데이터베이스 마이그레이션이 필요합니다.")
    @ColumnInfo
    val docId: String = ""
)

fun Int.toEmoticon(): Emoticon {
    return Emoticons.emoticons.find { it.id == this } ?: defaultEmoticon
}