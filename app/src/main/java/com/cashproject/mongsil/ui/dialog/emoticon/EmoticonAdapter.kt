package com.cashproject.mongsil.ui.dialog.emoticon

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ItemEmoticonBinding
import com.cashproject.mongsil.ui.model.Emoticon

class EmoticonAdapter(
    private val items: List<Emoticon>
) : RecyclerView.Adapter<EmoticonAdapter.ViewHolder>() {

//    private var items: List<Emoticon> = emptyList()

    private var listener: ((item: Emoticon) -> Unit)? = null

//    fun setEmoticons(emoticons: List<Emoticon>) {
//        items = emoticons
//        notifyDataSetChanged()
//    }

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

        @SuppressLint("ResourceType")
        fun bindView(item: Emoticon) {

            val context = itemView.context
            binding.emoticon = item

            Glide.with(context)
                .load(item.imageUrl)
                .into(binding.ivEmoticonIcon)

//            binding.ivEmoticonBackground.setBackgroundColor(
//                ContextCompat.getColor(
//                    context,
//                    Color.parseColor(item.background)
//                )
//            )
//            binding.tvEmoticonEmotion.setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    Color.parseColor(item.textColor)
//                )
//            )
            //이미지뷰 라운딩 처리
            binding.ivEmoticonRound.apply {
                background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.image_round_shape
                ) //이미지 라운딩 처리
                clipToOutline = true
            }

            binding.root.setOnClickListener {
                listener?.invoke(item) //익명함수 호출
            }
        }
    }

}