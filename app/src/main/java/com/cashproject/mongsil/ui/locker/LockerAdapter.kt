package com.cashproject.mongsil.ui.locker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ItemSayingBinding
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.util.image.GlideApp

class LockerAdapter : RecyclerView.Adapter<LockerAdapter.ViewHolder>() {

    private var items: ArrayList<LikeSaying> = ArrayList()

    private var listener: ((item: LikeSaying) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: LikeSaying) -> Unit) {
        this.listener = listener
    }

    fun update(newItemList: List<LikeSaying>) {
        val diffResult = DiffUtil.calculateDiff(ContentDiffUtil(items, newItemList), false)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItemList)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LockerAdapter.ViewHolder, position: Int) {
        holder.bindView(items[position])
        Log.d("Locker", items[position].toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
     = ViewHolder(ItemSayingBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    inner class ViewHolder(private val binding: ItemSayingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindView(item: LikeSaying){
            binding.saying = item
            GlideApp.with(itemView.context)
                .load(item.image)
                .centerCrop()
//            .placeholder(R.drawable.loading)
                .error(R.drawable.ic_baseline_terrain_24)
                .into(binding.itemSayingImage)
            binding.itemSayingImage.setOnClickListener{
                listener?.invoke(item) //익명함수 호출
            }
        }
    }

    class ContentDiffUtil(private val oldList: List<LikeSaying>, private val currentList: List<LikeSaying>) : DiffUtil.Callback() {

        //1. 아이템의 고유 id 값이 같은지
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].docId == currentList[newItemPosition].docId //title 대신 id 사용가능
        }

        //2. id 가 같다면, 내용물도 같은지 확인 equals()
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == currentList[newItemPosition]
        }

        //변화하기 전 데이터셋 사이즈
        override fun getOldListSize(): Int = oldList.size
        //변화한 후 데이터셋 사이즈
        override fun getNewListSize(): Int = currentList.size
    }
}