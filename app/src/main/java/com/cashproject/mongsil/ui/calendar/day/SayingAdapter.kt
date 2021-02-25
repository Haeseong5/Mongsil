package com.cashproject.mongsil.ui.calendar.day

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.ItemDayBinding
import com.cashproject.mongsil.databinding.ItemSayingBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.imagebind.GlideApp
import com.cashproject.mongsil.ui.locker.LockerAdapter

class SayingAdapter(val sayingCase: SayingCase) : RecyclerView.Adapter<SuperViewHolder>() {

    private var items: ArrayList<Saying> = ArrayList<Saying>()

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
            DiffUtil.calculateDiff(LockerAdapter.ContentDiffUtil(items, newItemList), false)
        diffResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperViewHolder =

        when (sayingCase) {
            SayingCase.LIST -> ListViewHolder(
                ItemDayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SayingCase.CALENDAR -> ListViewHolder(
                ItemDayBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SuperViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
                holder.bindView(items[position])
                Log.d("bind", items[position].imageRef.toString())
            }

            is CalendarViewHolder -> {
                holder.bindView(items[position])
            }
        }

    }
    inner class ListViewHolder(val binding: ItemDayBinding) : SuperViewHolder(binding) {


        fun bindView(item: Saying) {
            binding.saying = item

//            GlideApp.with(itemView.context)
//                .load(item.imageRef)
////            .placeholder(R.drawable.loading)
//                .error(R.drawable.ic_launcher_background)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(binding.itemSayingImage)
            binding.root.setOnClickListener {
                listener?.invoke(item) //익명함수 호출
            }
        }
    }

    inner class CalendarViewHolder(val binding: ItemDayBinding) : SuperViewHolder(binding) {
        fun bindView(item: Saying) {
            binding.saying = item

            binding.root.setOnClickListener {
                listener?.invoke(item) //익명함수 호출
            }
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