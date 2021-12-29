package com.cashproject.mongsil.ui.pages.locker

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentLockerBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.detail.DetailFragment
import java.util.*


class LockerFragment : BaseFragment<FragmentLockerBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_locker

    private val mainViewModel: MainViewModel by activityViewModels()

    private val lockerAdapter: LockerAdapter by lazy {
        LockerAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getAllLike()

        initToolbar()
        initRecyclerView()

        mainActivity?.mainViewModel?.likeList?.observe(viewLifecycleOwner, Observer {
            lockerAdapter.update(it as ArrayList<Saying>)
        })
    }

    //TODO 메뉴바텀시트 수정 필요
    override fun onResume() {
        super.onResume()
        mainViewModel.getAllLike()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbLocker)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.settingIcon.setOnClickListener {
            findNavController().navigate(R.id.action_to_setting)
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}