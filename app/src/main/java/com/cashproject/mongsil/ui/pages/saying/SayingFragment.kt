package com.cashproject.mongsil.ui.pages.saying

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentSayingBinding
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
import com.cashproject.mongsil.viewmodel.SayingViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import java.util.*


class SayingFragment : BaseFragment<FragmentSayingBinding, SayingViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_saying

    override val viewModel: SayingViewModel by viewModels { viewModelFactory }

    private lateinit var mSaying: Saying

    private val click by lazy { ClickUtil(this.lifecycle) }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    //adMob
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAd()
    }

    override fun initStartView() {
        initRecyclerView()
        initSaying()
        initClickListener()

        observeData()
    }

    private fun initSaying() {
        //보관함 or 리스트에서 넘어왔을 경우
        if (arguments != null) {
            val saying = arguments?.getParcelable<Saying>("saying")?.let {
                mSaying = it
                binding.saying = it
                viewModel.getComments(it.docId)
            }
            //calendar 에서 넘어왔을 경우
            if (saying == null) {
                arguments?.getString("docId").let {
                    viewModel.getSayingData(it!!)
                }
            }
        } else { //처음 실행했을 경우
            viewModel.getTodayData()
        }

        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
        d(TAG, emoticons[selectedEmoticonId].emotion + "이모티콘")
    }

    private fun initRecyclerView() {
        binding.rvSayingCommentList.apply {
            layoutManager = LinearLayoutManager(context)
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
                showBottomMenuDialog() //다이어로그 중복생성 방지
            }
        }

        binding.ivSayingEmoticon.setOnClickListener {
            click.run {
                showEmoticonBottomSheet() //다이어로그 중복생성 방지
            }
        }

        binding.tvSayingCommentBtn.setOnClickListener {
            viewModel.insertComment(
                Comment(
                    docId = mSaying.docId,
                    content = binding.etSayingCommentInput.text.toString(),
                    time = Date(),
                    date = mSaying.date,
                    emotion = selectedEmoticonId

                )
            )
            binding.etSayingCommentInput.text?.clear()
//            if (binding.etSayingCommentInput.text.isNullOrBlank()) {
//                activity?.showToast("일기를 입력해주세요.")
//            } else {
//
//            }
        }
    }

    private fun observeData() {
        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            mSaying = it
            viewModel.getComments(mSaying.docId)
        })

        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            commentAdapter.update(it as ArrayList<Comment>)
        })

        //댓글 삽입/삭제 결과 관찰
        viewModel.isCompletable.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getComments(mSaying.docId)
            }
        })

    }

    private fun showBottomMenuDialog() {
        val bottomSheetFragment =
            MenuBottomSheetFragment(mSaying)
        bottomSheetFragment.show(childFragmentManager, "approval")

        bottomSheetFragment.setLikeBtnOnClickListener {

        }

        bottomSheetFragment.setSaveBtnOnClickListener {
            //bitmap
            val bitmap = binding.ivSayingBackgroundImage.drawable as BitmapDrawable
            val imageUri = bitmap.bitmap.saveImage(requireActivity())

            if (imageUri != null) {
                activity?.showToast("갤러리에 이미지가 저장되었습니다.")
            } else {
                activity?.showToast("저장 실패")
            }
        }

        bottomSheetFragment.setHideCommentBtnOnClickListener {
            if (PreferencesManager.isVisibilityComment)
                binding.rvSayingCommentList.visibility = View.VISIBLE
            else
                binding.rvSayingCommentList.visibility = View.GONE
            bottomSheetFragment.setCommentIcon()
        }

        bottomSheetFragment.setShareBtnOnClickListener {
            shareToSNS()
        }
    }

    private fun showEmoticonBottomSheet() {
        val bottomSheetFragment = EmoticonDialog()
        bottomSheetFragment.show(childFragmentManager, "approval")
        bottomSheetFragment.setEmoticonBtnClickListener {
            selectedEmoticonId = it.id
            binding.ivSayingEmoticon.setImageResource(it.icon)
            bottomSheetFragment.dismiss()
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
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

    private fun createAd(){
        d("creatAD", "createAd")
        //게임에서 다음 레벨로 넘어갈 때 또는 작업을 완료한 직후가 광고를 게재하기 좋은 시점
        // Create the InterstitialAd and set it up.
        mInterstitialAd = InterstitialAd(requireActivity())
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712" // my id = ca-app-pub-1939032811151400/1834551535
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = (
                object : AdListener() {
                    override fun onAdLoaded() {
                        Toast.makeText(requireActivity(), "onAdLoaded()", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, " +
                                "message: ${loadAdError.message}"

                        d("onAdFailedToLoad", error)
                    }

                    override fun onAdClosed() {
                        Toast.makeText(requireActivity(), "onAdClosed()", Toast.LENGTH_SHORT).show()
                        mInterstitialAd.loadAd(AdRequest.Builder().build())

                    }
                }
            )

    }
}

