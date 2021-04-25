package com.cashproject.mongsil.ui.pages.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.extension.getImageUri
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Emoticons.emoticons
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.dialog.CheckDialog
import com.cashproject.mongsil.ui.dialog.MenuBottomSheetFragment
import com.cashproject.mongsil.ui.dialog.emoticon.EmoticonDialog
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.PreferencesManager.selectedEmoticonId
import com.cashproject.mongsil.util.RxEventBus
import com.cashproject.mongsil.viewmodel.HomeViewModel
import java.lang.Exception
import java.util.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private lateinit var mSaying: Saying //real data != comment data -> 초기화x -> 에러 발생

    private val click by lazy { ClickUtil(this.lifecycle) }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    //adMob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity?.adMobInitial()
        hasWriteStoragePermission()

    }

    override fun initStartView() {
        initRecyclerView()
        initSaying()
        initClickListener()

        observeData()
        observeErrorEvent()
    }

    //정리하기
    private fun initSaying() {
        arguments?.run {
            //보관함 or 리스트에서 넘어왔을 경우
            getParcelable<Saying>("saying")?.let {
                mSaying = it
                binding.saying = it
                viewModel.getCommentsForHome(it.docId)
            }
            //캘린더에서 넘어왔을 경우
            getString("docId").also {
                it?.also {
                    mainActivity?.mainViewModel?.getSingleSayingData(it)
                }
            }
        }

        if (selectedEmoticonId > 14){
            selectedEmoticonId = 0
        }
        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
    }

    private fun observeData() {
        mainActivity?.mainViewModel?.sayingForHome?.observe(viewLifecycleOwner, Observer {
            d(TAG, it.toString())
            binding.saying = it
            mSaying = it //null error
            viewModel.getCommentsForHome(mSaying.docId)
        })

        //댓글 불러오기
        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            d(TAG, it.toString())
            commentAdapter.update(it as ArrayList<Comment>)
            binding.rvSayingCommentList.scrollToPosition(it.size-1)
        })

        //댓글 삽입/삭제 결과 관찰
        viewModel.isUpdatedComment.observe(viewLifecycleOwner, Observer {
            if (it) {
                d(TAG, "isCompletable $it")
                viewModel.getCommentsForHome(mSaying.docId)
//                RxEventBus.sendToCalendar(true)
            }
        })

//        RxEventBus.toHomeObservable().subscribe({
//            if (it) viewModel.getComments(mSaying.docId)
//            Log.d(TAG, "RxEventBus Consume $it")
//        }, {
//            Log.i(TAG, it.message.toString())
//        }).addTo(compositeDisposable)
//
//        viewModel.loadingSubject
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                Log.d("Loading...", it.toString())
//                mainActivity?.progressBar?.isProgress(it)
//            }
//            .addTo(compositeDisposable)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, true)
        binding.rvSayingCommentList.apply {
            this.layoutManager = layoutManager
            setHasFixedSize(true)
            adapter = commentAdapter
        }
        commentAdapter.setOnItemLongClickListener {
            showCheckDialog(it.id)
        }
    }

    private fun initClickListener() {
        //button click listener
        binding.ivSayingBackgroundImage.setOnClickListener {
            click.run {
                if (mSaying.docId.isNotEmpty())
                    showBottomMenuDialog()
                else
                    requireActivity().showToast("네트워크 연결 상태를 확인해주세요.")
            }
        }

        binding.ivSayingEmoticon.setOnClickListener {
            click.run {
                showEmoticonBottomSheet()
            }
        }

        /**
         * 댓글 입력 버튼 클릭 시 호출. documentId가 없으면, 댓글 저장 실패
         */
        binding.tvSayingCommentBtn.setOnClickListener {
            try {
                if (mSaying.docId.isNotEmpty()){
                    viewModel.insertComment(
                        Comment(
                            docId = mSaying.docId,
                            content = binding.etSayingCommentInput.text.toString(),
                            time = Date(),
                            date = mSaying.date,
                            emotion = selectedEmoticonId
                        )
                    )
                } else{
//                    viewModel.deleteCommentById(mSaying.docId)
                    findNavController().popBackStack()
                }


            }catch (e: ExceptionInInitializerError){
                //잘못들어오면
//                viewModel.deleteCommentById(mSaying.docId)
            }
            binding.etSayingCommentInput.text?.clear()


        }
    }

    private fun showBottomMenuDialog() {
        val bottomSheetFragment = MenuBottomSheetFragment(mSaying)
        if (mSaying.docId != ""){
            bottomSheetFragment.show(childFragmentManager, "approval")
        }else{
            activity?.showToast("Saying's documentId is empty")
            Log.e(TAG, "Saying's documentId is empty")
        }

        bottomSheetFragment.setLikeBtnOnClickListener {
//            showAdMob()
//            RxEventBus.sendToLocker(true)
        }

        bottomSheetFragment.setSaveBtnOnClickListener {
            mainActivity?.showAdMob()
            //bitmap
            val bitmap = binding.ivSayingBackgroundImage.drawable as BitmapDrawable
            try {
                bitmap.bitmap.saveImage(requireActivity()).run {
                    activity?.showToast("갤러리에 이미지가 저장되었습니다.")
                }
            }catch (e: Exception){
                Log.e(TAG, e.message.toString())
                activity?.showToast("외부 저장소 쓰기 권한을 허용해주세요 ㅜㅜ.")
            }
        }

        bottomSheetFragment.setHideCommentBtnOnClickListener {
            if (PreferencesManager.isVisibilityComment)
                binding.rvSayingCommentList.visibility = View.VISIBLE
            else
                binding.rvSayingCommentList.visibility = View.GONE
        }

        bottomSheetFragment.setShareBtnOnClickListener {
            try {
                shareToSNS()
            }catch (e: Exception){
                Log.e(TAG, e.message.toString())
                activity?.showToast("외부 저장소 쓰기 권한을 허용해주세요 ㅜㅜ.")
            }
        }
    }

    private fun showEmoticonBottomSheet() {
        val bottomSheetFragment = EmoticonDialog()
        bottomSheetFragment.show(childFragmentManager, "approval")
        bottomSheetFragment.setEmoticonBtnClickListener {
            selectedEmoticonId = it.id
            binding.ivSayingEmoticon.setImageResource(it.icon)
            bottomSheetFragment.dismiss()
        }
    }

    private fun showCheckDialog(id: Int) {
        val dialog = CheckDialog(requireContext())
        dialog.setAcceptBtnOnClickListener {
            viewModel.deleteCommentById(id)
        }
        dialog.start(getString(R.string.message_delete))
    }

    private fun shareToSNS() {
        val bitmap = binding.ivSayingBackgroundImage.drawable as BitmapDrawable
        val imageUri = getImageUri(requireActivity(), bitmap.bitmap)
//        val imageUri = bitmap.bitmap.saveImage(requireActivity())

        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        val chooser = Intent.createChooser(intent, "친구에게 공유하기")
        startActivity(chooser)
    }

    private fun hasWriteStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100
                )
                return false
            }
        }
        return true
    }
}

