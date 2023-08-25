package com.cashproject.mongsil.ui.pages.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.BuildConfig
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.App
import com.cashproject.mongsil.data.firebase.isOldVersion
import com.cashproject.mongsil.databinding.FragmentSettingBinding
import com.cashproject.mongsil.databinding.FragmentSettingComposeBinding
import com.cashproject.mongsil.extension.intentAction
import com.cashproject.mongsil.extension.openPlayStore
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.ui.dialog.CheckDialog
import com.cashproject.mongsil.ui.main.IntroActivity
import com.cashproject.mongsil.ui.pages.setting.composable.SettingButtonType
import com.cashproject.mongsil.ui.pages.setting.composable.SettingScreen
import com.cashproject.mongsil.ui.pages.setting.composable.UiAction
import com.google.android.play.core.review.ReviewManagerFactory

//TODO 스플래시 끄기
//TODO 앱 버전 확인 -> 업데이트
// 푸시메세지
class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingComposeBinding
    private val viewModel = SettingViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingComposeBinding.inflate(inflater, container, false)
            .also { binding ->
                this.binding = binding
                binding.composeView.setContent {
                    SettingScreen(
                        onUiAction = {
                            when (it) {
                                UiAction.Back -> {
                                    findNavController().popBackStack()
                                }

                                is UiAction.OnClickMenu -> {
                                    when (it.type) {
                                        SettingButtonType.NOTIFICATION_SETTING -> startAlarm()
                                        SettingButtonType.APP_INTRODUCTION -> introApp()
                                        SettingButtonType.SUGGESTION -> sendEmail()
                                        SettingButtonType.BACKUP -> {
                                            requireContext().showToast("준비 중입니다.")
                                            backUp()
                                        }
                                        SettingButtonType.OTHER_APP_FROM_THE_DEVELOPER -> showMoreApps()
                                        SettingButtonType.APP_VERSION_CHECK -> showAppVersionDialog()
                                        SettingButtonType.NONE -> {}
                                    }

                                }
                            }
                        },
                    )
                }
                binding.lifecycleOwner = viewLifecycleOwner
            }.root
    }

    fun showAppVersionDialog() {
        if (isOldVersion()) {
            CheckDialog(
                context = requireContext(),
                accept = { openPlayStore(context ?: return@CheckDialog) },
                acceptText = "업데이트"
            ).also {
                it.start(
                    getString(
                        R.string.app_version,
                        BuildConfig.VERSION_NAME,
                        App.appVersion.latestAppVersionName
                    )
                )
            }
        } else {
            requireContext().showToast("최신 버전입니다.")
        }
    }

    fun backUp() {
        findNavController().navigate(R.id.action_to_backupFragment)
    }

    fun showReadyMessage() {
        activity?.showToast("준비 중입니다.")
    }

    fun showMoreApps() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/developer?id=Project+J+Lab")
        )
        startActivity(intent)
    }

    fun startLocker() {
        findNavController().navigate(R.id.action_setting_to_locker)
    }

    fun startAlarm() {
        findNavController().navigate(R.id.action_to_alarm)
    }

    fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.admin_email))) //받는 사람
            putExtra(Intent.EXTRA_SUBJECT, "몽실에게 건의하기") //제목
            putExtra(
                Intent.EXTRA_TEXT,
                "앱 버전 (App Version): ${BuildConfig.VERSION_NAME}\n" +
                        "Android (SDK): ${Build.VERSION.SDK_INT}\n" +
                        "Android Version(Release): ${Build.VERSION.RELEASE}\n" +
                        "내용: "
            )
            type = "message/rfc822"
            startActivity(this)
        }
    }

    fun writeInAppReview() {
        val manager = ReviewManagerFactory.create(requireActivity())
//        val manager = FakeReviewManager(context)
        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                Log.d("In App Review Info", reviewInfo.toString())
                reviewInfo.let {
                    val flow = manager.launchReviewFlow(requireActivity(), it)
                    flow.addOnCompleteListener {
                        //Irrespective of the result, the app flow should continue
                        if (it.isSuccessful) activity?.showToast("소중한 리뷰 감사합니다!")
                    }
                }
            } else {
                // There was some problem, log or handle the error code.
                val reviewErrorCode = task.exception
                Log.e("In App Review Error", reviewErrorCode.toString())
            }
        }
    }

    fun introApp() {
        intentAction(IntroActivity::class)
    }
}