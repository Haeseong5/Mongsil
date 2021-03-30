package com.cashproject.mongsil.ui.pages.calendar.day

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cashproject.mongsil.databinding.ItemDayBinding
import com.cashproject.mongsil.databinding.ItemLoadingBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.util.DateUtil
import kotlin.collections.ArrayList

class DayAdapter(private val viewTypeCase: ViewTypeCase) : RecyclerView.Adapter<SuperViewHolder>() {

    private var isLoaderVisible = false

    private var items: ArrayList<Saying> = ArrayList()

    private var listener: ((item: Saying) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: Saying) -> Unit) {
        this.listener = listener
    }

    fun setItems(items: ArrayList<Saying>) {
        this.items = items
        notifyDataSetChanged()
        Log.d("Size", items.size.toString())
    }

    fun update(newItemList: ArrayList<Saying>) {
        val diffResult =
            DiffUtil.calculateDiff(DayAdapter.ContentDiffUtil(items, newItemList), false)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItemList)
    }

    fun add(saying: Saying) {
        items.add(saying)
        notifyItemInserted(items.size-1) //삽입된 위치만 갱신
    }

    fun isLoading(flag: Boolean, newItemList: ArrayList<Saying>){
        isLoaderVisible = flag
        update(newItemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperViewHolder =

        when (viewTypeCase) {
            ViewTypeCase.NORMAL -> ListViewHolder(
                ItemDayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ViewTypeCase.LOADING -> FooterHolder(
                ItemLoadingBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SuperViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (isLoaderVisible) {
            val lastPosition = items.size - 1
            return if (position == lastPosition) ViewTypeCase.LOADING.index() else ViewTypeCase.NORMAL.index()
        } else {
            return ViewTypeCase.NORMAL.index()
        }
    }

    inner class ListViewHolder(val binding: ItemDayBinding) : SuperViewHolder(binding) {

        override fun bindView(item: Saying) {
            binding.saying = item

            val dateArray = item.date.let { DateUtil.dateToStringArray(it) }
            binding.tvDayYear.text = dateArray[0]
            binding.tvDayMonth.text = dateArray[1]
            binding.tvDayDate.text = dateArray[2]


            binding.root.setOnClickListener {
                listener?.invoke(item) //익명함수 호출
            }
        }

        override fun clear() {
            TODO("Not yet implemented")
        }
    }

    inner class FooterHolder(val binding: ItemLoadingBinding) : SuperViewHolder(binding) {
        override fun bindView(item: Saying) {

            binding.root.setOnClickListener {
                listener?.invoke(item) //익명함수 호출
            }
        }

        override fun clear() {
            TODO("Not yet implemented")
        }
    }

    class ContentDiffUtil(
        private val oldList: List<Saying>,
        private val currentList: List<Saying>
    ) : DiffUtil.Callback() {

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