package com.cashproject.mongsil.ui.locker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.databinding.ItemSayingBinding
import com.cashproject.mongsil.model.data.Saying

class LockerAdapter : RecyclerView.Adapter<LockerAdapter.ViewHolder>() {

    private lateinit var items: ArrayList<Saying>

    private var listener: ((item: Saying) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: Saying) -> Unit) {
        this.listener = listener
    }

    fun setItems(items: ArrayList<Saying>){
        this.items = items
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LockerAdapter.ViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
     = ViewHolder(ItemSayingBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    inner class ViewHolder(private val binding: ItemSayingBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindView(item: Saying){
            binding.saying = item

            binding.itemSayingImage.setOnClickListener{
                listener?.invoke(item) //익명함수 호출
            }
        }
    }

    class ContentDiffUtil(private val oldList: List<Saying>, private val currentList: List<Saying>) : DiffUtil.Callback() {

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