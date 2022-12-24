package com.cashproject.mongsil.ui.pages.locker

import android.util.Log.v
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.databinding.ItemLockerBinding
import com.cashproject.mongsil.data.db.entity.SayingEntity

/**
 * diffUtil 다른거 써 보기
 */
class LockerAdapter : RecyclerView.Adapter<LockerAdapter.ViewHolder>() {
    private val TAG = this.javaClass.simpleName
    private var items: ArrayList<SayingEntity> = ArrayList()

    private var listener: ((item: SayingEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: SayingEntity) -> Unit) {
        this.listener = listener
    }

    fun update(newItemList: List<SayingEntity>) {
        val diffResult = DiffUtil.calculateDiff(ContentDiffUtil(items, newItemList), false)
        diffResult.dispatchUpdatesTo(this) //Dispatches the update events to the given adapter. 주어진 어댑터에 변경사항을 전달한다.
        items.clear()
        items.addAll(newItemList)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LockerAdapter.ViewHolder, position: Int) {
        holder.bindView(items[position])
        v(TAG, "onBindViewHolder")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        v(TAG, "onCreateViewHolder")
        return ViewHolder(ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class ViewHolder(private val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindView(item: SayingEntity){
            binding.saying = item
            binding.ivLockerImage.setOnClickListener{
                listener?.invoke(item) //익명함수 호출
            }
        }
    }

    inner class ContentDiffUtil(private val oldList: List<SayingEntity>, private val currentList: List<SayingEntity>) : DiffUtil.Callback() {

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