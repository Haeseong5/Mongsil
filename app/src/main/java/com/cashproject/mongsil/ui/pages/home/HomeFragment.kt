package com.cashproject.mongsil.ui.pages.home

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.util.Log.i
import android.view.View
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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
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
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adMobInitial()
    }

    override fun initStartView() {
        initRecyclerView()
        initSaying()
        initClickListener()

        observeData()
        observeErrorEvent()
    }

    private fun initSaying() {
        //이부분 정리하기 ;
        //보관함 or 리스트에서 넘어왔을 경우
        if (arguments != null) {
            val saying = arguments?.getParcelable<Saying>("saying")?.let {
                mSaying = it
                binding.saying = it
                viewModel.getComments(it.docId)
            }
            //calendar 에서 넘어왔을 경우
            if (saying == null) {
                arguments?.getString("docId").also {
                    it?.also {
                        viewModel.getSingleSayingData(it)
                    }
                }
                //doc id null이면 댓글 날리기
                //                    viewModel.deleteCommentById(mSaying.docId)
//                findNavController().popBackStack()
            }
        } else { //처음 실행했을 경우
            viewModel.getTodayData()
        }

        if (selectedEmoticonId > 14){
            selectedEmoticonId = 0
        }
        binding.ivSayingEmoticon.setImageResource(emoticons[selectedEmoticonId].icon)
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
                showBottomMenuDialog() //다이어로그 중복생성 방지
            }
        }

        binding.ivSayingEmoticon.setOnClickListener {
            click.run {
                showEmoticonBottomSheet() //다이어로그 중복생성 방지
            }
        }

        /**
         * 댓글 입력 버튼 클릭 시 호출. documentId가 없으면, 댓글 저장 실패
         */
        binding.tvSayingCommentBtn.setOnClickListener {
            try {
                if (mSaying.docId != ""){
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

    private fun observeData() {
        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            mSaying = it //null error
            isProgress(false)
            viewModel.getComments(mSaying.docId)

            if(it == null){
              mSaying = Saying()
            }
        })

        //댓글 데이터 불러오기
        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            commentAdapter.update(it as ArrayList<Comment>)
            binding.rvSayingCommentList.scrollToPosition(it.size-1)
        })

        //댓글 삽입/삭제 결과 관찰
        viewModel.isCompletable.observe(viewLifecycleOwner, Observer {
            if (it) {
                d(TAG, "isCompletable $it")
                viewModel.getComments(mSaying.docId)
                RxEventBus.sendToCalendar(true)
            }
        })

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
            RxEventBus.sendToLocker(true)
        }

        bottomSheetFragment.setSaveBtnOnClickListener {
            showAdMob()

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

    private fun showAdMob(){
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            i("TAG", "The interstitial wasn't loaded yet.");
        }
    }
    private fun adMobInitial(){
        // Create the InterstitialAd and set it up.
        mInterstitialAd = InterstitialAd(requireActivity())
        mInterstitialAd.adUnitId = getString(R.string.sample_ad_interstitial_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = (
                object : AdListener() {
                    override fun onAdLoaded() {
                        i("onAdFailedToLoad", "onAdLoaded()")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, " +
                                "message: ${loadAdError.message}"

                        i("onAdFailedToLoad", error)
                    }

                    override fun onAdClosed() {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                    }
                }
            )

    }
}

