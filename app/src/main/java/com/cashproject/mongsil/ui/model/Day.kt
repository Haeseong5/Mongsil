package com.cashproject.mongsil.ui.model

import com.cashproject.mongsil.data.db.entity.CommentEntity
import java.util.*

data class Day (
    var calendar: Calendar,
    var commentEntities : List<CommentEntity>
)