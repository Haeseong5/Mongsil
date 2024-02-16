package com.cashproject.mongsil.ui.model

import com.cashproject.mongsil.database.model.CommentEntity
import java.util.Calendar

data class Day (
    var calendar: Calendar,
    var commentEntities : List<CommentEntity>
)