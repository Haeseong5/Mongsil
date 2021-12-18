package com.cashproject.mongsil.ui.pages.locker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentLockerBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.detail.DetailFragment
import java.util.*
import kotlin.collections.ArrayList


class LockerFragment : BaseFragment<FragmentLockerBinding, LockerViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_locker

    override val viewModel: LockerViewModel by viewModels { viewModelFactory }

    private val lockerAdapter: LockerAdapter by lazy {
        LockerAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initStartView() {
        initToolbar()
        initRecyclerView()
        viewModel.getAllLike()

        observeData()
        observeErrorEvent()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbLocker)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initRecyclerView() {
        binding.rvLockerList.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = lockerAdapter
        }

        lockerAdapter.setOnItemClickListener {
            click.run {
                DetailFragment.start(
                    fragment = this,
                    argument = DetailFragment.Argument(
                        saying = it,
                        selectedDate = Date(),
                        from = "locker",
                    ),
                )
            }
        }
    }

    private fun observeData() {
        viewModel.likeData.observe(viewLifecycleOwner, Observer {
            lockerAdapter.update(it as ArrayList<Saying>)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllLike()
    }

}