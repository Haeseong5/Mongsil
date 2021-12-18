package com.cashproject.mongsil.ui.pages.calendar.day

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.model.data.Saying

//뷰홀더 추상화
abstract class SuperViewHolder (binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
{
    open fun bindView(item: Saying, position: Int){

    }

    abstract fun clear()
}