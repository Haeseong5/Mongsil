package com.cashproject.mongsil.ui.pages.detail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
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
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.util.PermissionUtil.hasWriteStoragePermission
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.PreferencesManager.selectedEmoticonId
import com.cashproject.mongsil.util.isSameDay
import kotlinx.parcelize.Parcelize
import java.util.*

class DetailFragment : BaseFragment<FrammentDetailBinding, DetailViewModel>() {

    companion object {
        private const val NAV_ID = R.id.action_global_detailFragment

        fun start(
            fragment: Fragment,
            argument: Argument,
            navOptions: NavOptions? = null
        ) {
            try {
                fragment.findNavController().navigate(
                    NAV_ID, bundleOf("argument" to argument), navOptions
                )
            } catch (e: Exception) {

            }
        }
    }

    @Parcelize
    data class Argument(
        val saying: Saying,
        val selectedDate: Date,
        val from: String? = null
    ) : Parcelable

    private val argument: Argument by lazy {
        arguments?.getParcelable<Argument>("argument")
            ?: throw IllegalArgumentException("Argument must exist")
    }

    override val layoutResourceId: Int
        get() = R.layout.framment_detail

    override val viewModel: DetailViewModel by viewModels { viewModelFactory }
    private val mainViewModel: MainViewModel by activityViewModels()

    private val commentAdapter: CommentAdapter by lazy { CommentAdapter() }

    val currentImageUrl get() = argument.saying.image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity?.adMobInitial()
        hasWriteStoragePermission(requireActivity())
    }

    override fun initStartView() {
        binding.fragment = this
        binding.mainViewModel = mainViewModel

        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
        if (argument.from == "locker") {
            binding.llSayingComment.visibility = View.GONE
        }
        binding.rvSayingCommentList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            setHasFixedSize(true)
            adapter = commentAdapter
        }

        commentAdapter.setOnItemLongClickListener {
            showCheckDialog(it.id)
        }

        observeData()
    }

    private fun observeData() {
        mainActivity?.mainViewModel?.commentList?.observe(viewLifecycleOwner, {
            it.filter { comment ->
                isSameDay(comment.date, argument.selectedDate)
            }.let { comments ->
                commentAdapter.update(comments)
                Handler(Looper.getMainLooper()).post {
                    binding.rvSayingCommentList.scrollToPosition(comments.size - 1)
                }
            }
        })

//        mainActivity?.mainViewModel?.likeList?.observe(viewLifecycleOwner, {
//            it.contains(argument.saying)
//        })
    }

    fun onClickBackgroundImage() {
        showBottomMenuDialog()
    }

    fun onClickEmoticon() {
        click.run {
            showEmoticonBottomSheet()
        }
    }

    fun onClickBackBtn() {
        findNavController().popBackStack()
    }

    /**댓글 입력 버튼 클릭 시 호출. documentId가 없으면, 댓글 저장 실패*/
    fun onClickSubmitComment() {
        mainViewModel.insertComment(
            Comment(
                content = binding.etSayingCommentInput.text.toString(),
                time = Date(),
                date = argument.selectedDate,
                emotion = selectedEmoticonId,
                docId = argument.saying.docId
            )
        )
        binding.etSayingCommentInput.text?.clear()
    }

    private fun showBottomMenuDialog() {
        MenuBottomSheetFragment(
            saying = argument.saying ?: return,
            selectedDate = argument.selectedDate
        ).apply {
            setSaveBtnOnClickListener {
                mainActivity?.showAdMob()
                //bitmap
                val bitmap =
                    this@DetailFragment.binding.ivSayingBackgroundImage.drawable as BitmapDrawable
                try {
                    bitmap.bitmap.saveImage(requireActivity()).run {
                        activity?.showToast("갤러리에 이미지가 저장되었습니다.")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    activity?.showToast("외부 저장소 쓰기 권한을 허용해주세요 ㅜㅜ.")
                }
            }
            setHideCommentBtnOnClickListener {
                if (PreferencesManager.isVisibilityComment)
                    this@DetailFragment.binding.rvSayingCommentList.visibility = View.VISIBLE
                else
                    this@DetailFragment.binding.rvSayingCommentList.visibility = View.GONE
            }
            setShareBtnOnClickListener {
                try {
                    shareToSNS()
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    activity?.showToast("외부 저장소 쓰기 권한을 허용해주세요 ㅜㅜ.")
                }
            }
        }.show(childFragmentManager, "approval")
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
            mainViewModel.deleteCommentById(id)
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

