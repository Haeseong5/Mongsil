package com.cashproject.mongsil.ui.pages.home

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.cashproject.mongsil.ui.pages.home.detail.HomeViewModel
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.PermissionUtil.hasWriteStoragePermission
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.PreferencesManager.selectedEmoticonId
import java.util.*

/**
 * 캘린더 갱신만 되면 됨. 무조건 오늘 명언만
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private var todaySaying: Saying? = null

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
        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
        observeData()
    }

    private fun observeData() {
        mainActivity?.mainViewModel?.sayingForHome?.observe(viewLifecycleOwner, Observer {
            d(TAG, "[observeData] docID: ${it.docId} date: ${it.date}")
            binding.saying = it
            todaySaying = it //null error
            todaySaying?.docId?.let { docId -> viewModel.getCommentsForHome(docId) }
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
                todaySaying?.docId?.let { docId -> viewModel.getCommentsForHome(docId) }
//                RxEventBus.sendToCalendar(true)
            }
        })

        //MainFragment 가 onResume 될 때 댓글 갱신? 혹은 댓글 변경 시
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
            todaySaying?.let {
                showBottomMenuDialog()
            }
        }
    }

    fun onClickEmoticon(){
        click.run {
            showEmoticonBottomSheet()
        }
    }

    /**댓글 입력 버튼 클릭 시 호출. documentId가 없으면, 댓글 저장 실패*/
    fun onClickSubmitComment() {
        todaySaying?.let {
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
        todaySaying?.let {
            val bottomSheetFragment = MenuBottomSheetFragment(it)
            if (it.docId.isNotEmpty()){
                bottomSheetFragment.show(childFragmentManager, "approval")
            }else{
                activity?.showToast("Saying's documentId is empty")
            }
//        bottomSheetFragment.setLikeBtnOnClickListener {}

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

    override fun onResume() {
        super.onResume()
        todaySaying?.docId?.let { docId -> viewModel.getCommentsForHome(docId) }
    }
}

