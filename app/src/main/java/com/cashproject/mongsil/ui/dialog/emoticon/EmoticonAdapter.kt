package com.cashproject.mongsil.ui.dialog.emoticon

import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ItemEmoticonBinding
import com.cashproject.mongsil.model.data.Emoticon
import com.cashproject.mongsil.model.data.Emoticons.emoticons

class EmoticonAdapter : RecyclerView.Adapter<EmoticonAdapter.ViewHolder>() {

    private var items: ArrayList<Emoticon> = emoticons

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


    inner class ViewHolder(private val binding: ItemEmoticonBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Emoticon) {

            binding.emoticon = item

            binding.ivEmoticonIcon.setImageResource(item.icon)

            binding.ivEmoticonBackground.setBackgroundColor(ContextCompat.getColor(itemView.context, item.background))
            binding.tvEmoticonEmotion.setTextColor(ContextCompat.getColor(itemView.context, item.textColor))
            //이미지뷰 라운딩 처리
            binding.ivEmoticonRound.apply {
                background = ContextCompat.getDrawable(itemView.context, R.drawable.image_round_shape) //이미지 라운딩 처리
                clipToOutline = true
            }

            binding.root.setOnClickListener {
                listener?.invoke(item) //익명함수 호출
            }
        }
    }

}