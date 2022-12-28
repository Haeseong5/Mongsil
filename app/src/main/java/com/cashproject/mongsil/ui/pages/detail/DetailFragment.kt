package com.cashproject.mongsil.ui.pages.detail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.manager.showInterstitialAd
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FrammentDetailBinding
import com.cashproject.mongsil.extension.getImageUri
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.domain.model.Emoticons.emoticons
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.ui.dialog.CheckDialog
import com.cashproject.mongsil.ui.dialog.MenuBottomSheetFragment
import com.cashproject.mongsil.ui.dialog.emoticon.EmoticonDialog
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.util.PermissionUtil.hasWriteStoragePermission
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.PreferencesManager.selectedEmoticonId
import com.cashproject.mongsil.util.isSameDay
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.parcelize.Parcelize
import java.util.*


class DetailFragment : BaseFragment<FrammentDetailBinding>() {

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
        val sayingEntity: SayingEntity,
        val selectedDate: Date,
        val from: String? = null
    ) : Parcelable

    private val argument: Argument by lazy {
        arguments?.getParcelable<Argument>("argument")
            ?: throw IllegalArgumentException("Argument must exist")
    }

    override val layoutResourceId: Int
        get() = R.layout.framment_detail

    private val mainViewModel: MainViewModel by activityViewModels()

    private val commentAdapter: CommentAdapter by lazy { CommentAdapter() }

    val currentImageUrl get() = argument.sayingEntity.image

    var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInterstitialAd()
        hasWriteStoragePermission(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)

        when (argument.from) {
            "locker" -> {
                binding.llSayingComment.visibility = View.GONE
            }
            "home" -> {
                binding.ivDetailFinish.visibility = View.GONE
            }
        }

        binding.rvSayingCommentList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            setHasFixedSize(true)
            adapter = commentAdapter
        }

        commentAdapter.setOnItemLongClickListener {
            showCheckDialog(it.id)
        }

        mainActivity?.mainViewModel?.commentEntityList?.observe(viewLifecycleOwner) {
            it.filter { comment ->
                isSameDay(comment.date, argument.selectedDate)
            }.let { comments ->
                commentAdapter.update(comments)
                binding.rvSayingCommentList.scrollToPosition(comments.size - 1)
            }
        }

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

    fun onClickSubmitComment() {
        mainViewModel.insertComment(
            CommentEntity(
                content = binding.etSayingCommentInput.text.toString(),
                time = Date(),
                emotion = selectedEmoticonId,
                date = argument.selectedDate
            )
        )
        binding.etSayingCommentInput.text?.clear()
    }

    private fun showBottomMenuDialog() {
        click.run {
            MenuBottomSheetFragment(
                sayingEntity = argument.sayingEntity ?: return@run,
                selectedDate = argument.selectedDate
            ).apply {
                setSaveBtnOnClickListener {
                    showInterstitialAd(mInterstitialAd, requireActivity())
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
                        this@DetailFragment.binding.llSayingComment.visibility = View.VISIBLE
                    else
                        this@DetailFragment.binding.llSayingComment.visibility = View.GONE
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
        CheckDialog(
            context = requireContext(),
            accept = { mainViewModel.deleteCommentById(id) }
        ).also {
            it.start(getString(R.string.message_delete))
        }
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
        //임시로 이모티콘 갱신
        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
    }

    private fun initInterstitialAd() {
        val adRequest: AdRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),
            requireContext().getString(R.string.ad_interstitial_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    Log.i(TAG, "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                }
            })
    }
}

