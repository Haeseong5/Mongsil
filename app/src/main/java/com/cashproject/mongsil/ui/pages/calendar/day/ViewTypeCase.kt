package com.cashproject.mongsil.ui.pages.calendar.day

interface Positionable{
    fun index() : Int
}
enum class ViewTypeCase: Positionable{
    LOADING{
        override fun index(): Int = 0
    },
    NORMAL{
        override fun index(): Int = 1
    }
}