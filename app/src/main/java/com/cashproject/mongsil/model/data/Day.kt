package com.cashproject.mongsil.model.data

import com.cashproject.mongsil.data.db.entity.Comment
import java.util.*

data class Day (
    var calendar: Calendar,
    var comments : List<Comment>
)