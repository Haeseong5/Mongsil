package com.cashproject.mongsil.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.databinding.ItemCommentBinding
import com.cashproject.mongsil.model.data.Comment

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private var items: ArrayList<Comment> = ArrayList()

    private var listener: ((item: Comment) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: Comment) -> Unit) {
        this.listener = listener
    }

    fun setItems(items: ArrayList<Comment>){
        this.items = items
        notifyDataSetChanged()
    }

    fun update(newItemList: List<Comment>) {
        val diffResult = DiffUtil.calculateDiff(ContentDiffUtil(items, newItemList), false)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItemList)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bindView(items[position])
        Log.d("Locker", items[position].toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
     = ViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    inner class ViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindView(item: Comment){
            binding.comment = item
        }
    }

    class ContentDiffUtil(private val oldList: List<Comment>, private val currentList: List<Comment>) : DiffUtil.Callback() {

        //1. 아이템의 고유 id 값이 같은지
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == currentList[newItemPosition].id //title 대신 id 사용가능
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