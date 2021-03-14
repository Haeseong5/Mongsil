package com.cashproject.mongsil.ui.pages.locker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.databinding.ItemLockerBinding
import com.cashproject.mongsil.model.data.Saying

class LockerAdapter : RecyclerView.Adapter<LockerAdapter.ViewHolder>() {

    private var items: ArrayList<Saying> = ArrayList()

    private var listener: ((item: Saying) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: Saying) -> Unit) {
        this.listener = listener
    }

    fun update(newItemList: List<Saying>) {
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
     = ViewHolder(ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    inner class ViewHolder(private val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindView(item: Saying){
            binding.saying = item
            binding.ivLockerImage.setOnClickListener{
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