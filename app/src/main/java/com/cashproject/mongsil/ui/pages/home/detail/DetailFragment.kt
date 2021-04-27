package com.cashproject.mongsil.ui.pages.home.detail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.databinding.FrammentDetailBinding
import com.cashproject.mongsil.extension.getImageUri
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Emoticons.emoticons
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.dialog.CheckDialog
import com.cashproject.mongsil.ui.dialog.MenuBottomSheetFragment
import com.cashproject.mongsil.ui.dialog.emoticon.EmoticonDialog
import com.cashproject.mongsil.ui.pages.home.CommentAdapter
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.PermissionUtil.hasWriteStoragePermission
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.PreferencesManager.selectedEmoticonId
import java.util.*

class DetailFragment : BaseFragment<FrammentDetailBinding, HomeViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.framment_detail

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private var mSaying: Saying? = null

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity?.adMobInitial()
        hasWriteStoragePermission(requireActivity())
    }

    override fun initStartView() {
        binding.fragment = this
        initRecyclerView()
        //set emoticon
        initSaying()
        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
        observeData()
    }

    fun initSaying(){
        arguments?.run {
            //보관함 or 리스트에서 넘어왔을 경우. 넘겨준 데이터로 바로 세팅
            getParcelable<Saying>("saying")?.let {
                mSaying = it
                binding.saying = it
                viewModel.getCommentsForHome(it.docId)
            }
            //캘린더에서 넘어왔을 경우. 파이어스토어에 요청
            getString("docId").also {
                it?.also {
                    viewModel.getSingleSayingData(it)
                }
            }
        }
    }

    private fun observeData() {
        viewModel.sayingLiveData.observe(viewLifecycleOwner, Observer {
            d(TAG, "[observeData] docID: ${it.docId} date: ${it.date}")
            binding.saying = it
            mSaying = it //null error
            mSaying?.docId?.let { docId -> viewModel.getCommentsForHome(docId) }
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
                mSaying?.docId?.let { docId -> viewModel.getCommentsForHome(docId) }
//                RxEventBus.sendToCalendar(true)
            }
        })
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

    fun onClickBackgroundImage(){
        d("onClickBack", "!!")
        click.run {
            mSaying?.let {
                showBottomMenuDialog()
            }
        }
    }

    fun onClickEmoticon(){
        click.run {
            showEmoticonBottomSheet()
        }
    }

    fun onClickBackBtn(){
        findNavController().popBackStack()
    }

    /**댓글 입력 버튼 클릭 시 호출. documentId가 없으면, 댓글 저장 실패*/
    fun onClickSubmitComment() {
        mSaying?.let {
            if (it.docId.isNotEmpty()){
                viewModel.insertComment(
                    Comment(
                        docId = it.docId,
                        content = binding.etSayingCommentInput.text.toString(),
                        time = Date(),
                        date = it.date,
                        emotion = selectedEmoticonId
                    )
                )
            }
        }
        binding.etSayingCommentInput.text?.clear()
    }


    private fun showBottomMenuDialog() {
        mSaying?.let {
            val bottomSheetFragment = MenuBottomSheetFragment(it)
            if (it.docId.isNotEmpty()){
                bottomSheetFragment.show(childFragmentManager, "approval")
            }else{
                activity?.showToast("Saying's documentId is empty")
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

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        val chooser = Intent.createChooser(intent, "친구에게 공유하기")
        startActivity(chooser)
    }
}

