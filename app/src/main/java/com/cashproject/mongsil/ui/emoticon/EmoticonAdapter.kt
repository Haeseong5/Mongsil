package com.cashproject.mongsil.ui.emoticon

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ItemEmoticonBinding
import com.cashproject.mongsil.databinding.ItemSayingBinding
import com.cashproject.mongsil.model.data.Emoticon
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.locker.LockerAdapter
import com.cashproject.mongsil.util.image.GlideApp

class EmoticonAdapter(private var items: ArrayList<Emoticon>) : RecyclerView.Adapter<EmoticonAdapter.ViewHolder>() {

    private var listener: ((item: Emoticon) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: Emoticon) -> Unit) {
        this.listener = listener
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: EmoticonAdapter.ViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemEmoticonBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    inner class ViewHolder(private val binding: ItemEmoticonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Emoticon) {

            binding.emoticon = item
//
//            binding.itemSayingImage.setOnClickListener {
//                listener?.invoke(item) //익명함수 호출
//            }
        }
    }

}