package gun0912.ted.tedadmobdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaContent;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * https://developers.google.com/admob/android/native/start
 * https://developers.google.com/admob/android/migration
 * https://github.com/googleads/googleads-mobile-android-examples/blob/master/java/admob/NativeAdvancedExample/app/src/main/java/com/google/example/gms/nativeadvancedexample/MainActivity.java
 */

public class TedAdmobDialog extends AlertDialog {
    private static final String TAG = "ted";
    private NativeAdView nativeAdView;
    private ProgressBar progressView;
    private LinearLayout bannerContainer;
    private final Builder builder;
    private NativeAd mNativeAd;

    public TedAdmobDialog(Builder builder, int theme) {
        super(builder.context, theme);
        this.builder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ted", "onCreate()");
        setContentView(R.layout.dialog_tedadmob);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(false);
        initView();

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                progressView.setVisibility(View.VISIBLE);
                bannerContainer.setVisibility(View.GONE);
                bannerContainer.removeAllViews();
                nativeAdView.setVisibility(View.GONE);
                switch (builder.adType) {
                    case AdType.BANNER:
                        showBanner(bannerContainer);
                        break;
                    case AdType.NATIVE:
                        showNative();
                        break;
                }
            }
        });
    }

    private void initView() {
        nativeAdView = findViewById(R.id.nativeAdView);
        progressView = findViewById(R.id.progressView);
        bannerContainer = findViewById(R.id.view_banner_container);
        findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishClick();
            }
        });


        TextView tvReview = findViewById(R.id.tv_review);
        View viewBtnDivider = findViewById(R.id.view_btn_divider);

        if (builder.showReviewButton) {
            tvReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReviewClick();
                }
            });
        } else {
            tvReview.setVisibility(View.GONE);
            viewBtnDivider.setVisibility(View.GONE);
        }
    }

    private void onFinishClick() {
        if (builder.onBackPressListener != null) {
            builder.onBackPressListener.onFinish();
        }
        dismiss();
    }

    private void onReviewClick() {
        openPlayStore();
        if (builder.onBackPressListener != null) {
            builder.onBackPressListener.onReviewClick();
        }
    }

    private void openPlayStore() {
        String packageName = getContext().getPackageName();
        try {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    private void showNative() {

        if (mNativeAd != null) {
            bindNativeView(mNativeAd, nativeAdView);
            mNativeAd = null;
        } else {
            loadNative(false);
        }

    }


    public void loadNative() {
        loadNative(true);
    }

    private void loadNative(final boolean preLoad) {
        Log.d(TAG, "loadNative()");
        AdLoader.Builder adLoaderBuilder = new AdLoader.Builder(getContext(), builder.unitId);
        adLoaderBuilder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        TedAdmobDialog.this.mNativeAd = nativeAd;
                        if (!preLoad) {
                            showNative();
                        }
                        mNativeAd = nativeAd;
                        if (!preLoad) {
                            showNative();
                        }
                    }
                });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(this.builder.startMute)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        adLoaderBuilder.withNativeAdOptions(adOptions);

        AdLoader adLoader = adLoaderBuilder.withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        @SuppressLint("DefaultLocale") String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(),
                                        loadAdError.getCode(),
                                        loadAdError.getMessage()
                                );
                        Log.e(this.getClass().getSimpleName(), "Failed to load native ad with error " + error);
                    }
                }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        AdRequest adRequest = new AdRequest.Builder().build();
        adLoader.loadAd(adRequest);
    }


    private void showBanner(LinearLayout bannerContainer) {
        AdView admobBannerView = new AdView(getContext());
        AdRequest adRequest = new AdRequest.Builder().build();

        admobBannerView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        admobBannerView.setAdUnitId(builder.unitId);
        admobBannerView.setAdListener(builder.adListener);

        admobBannerView.loadAd(adRequest);
        bannerContainer.addView(admobBannerView);
        bannerContainer.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
        if (builder.onBackPressListener != null) {
            builder.onBackPressListener.onAdShow();
        }
    }

    private void bindNativeView(NativeAd nativeAd, NativeAdView adView) {
        Log.d(TAG, "bindNativeView() NativeContentAd");
        MediaContent mediaContent = nativeAd.getMediaContent();

        ImageView ivImage = adView.findViewById(R.id.iv_image);
        MediaView mediaView = adView.findViewById(R.id.mediaView);

        ivImage.setVisibility(View.GONE);
        mediaView.setVisibility(View.GONE);
        List<NativeAd.Image> images = nativeAd.getImages();

        VideoController vc = mediaContent.getVideoController();

        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mediaView.setVisibility(View.VISIBLE);
        } else if (images != null && !images.isEmpty()) {
            adView.setImageView(ivImage);
            ivImage.setVisibility(View.VISIBLE);
            ivImage.setImageDrawable(images.get(0).getDrawable());
        }


        adView.setHeadlineView(adView.findViewById(R.id.tv_name));
        adView.setBodyView(adView.findViewById(R.id.tv_body));
        adView.setCallToActionView(adView.findViewById(R.id.tv_call_to_action));
//        adView.setLogoView(adView.findViewById(R.id.iv_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.tv_etc));

        adView.setIconView(adView.findViewById(R.id.iv_logo));
        adView.setStoreView(adView.findViewById(R.id.tv_etc));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            adView.getStoreView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
        nativeAdView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);

        if (builder.onBackPressListener != null) {
            builder.onBackPressListener.onAdShow();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({AdType.NATIVE, AdType.BANNER})
    public @interface AdType {
        int NATIVE = 1;
        int BANNER = 2;
    }

    public static class Builder {
        private final Context context;
        @AdType
        private final int adType;
        private final String unitId;
        private boolean startMute = true;
        private AdListener adListener;
        private OnBackPressListener onBackPressListener;
        private boolean showReviewButton = true;

        public Builder(Context context, @AdType int adType, String unitId) {
            this.context = context;
            this.adType = adType;
            this.unitId = unitId;
        }

        public Builder setStartMute(boolean startMute) {
            this.startMute = startMute;
            return this;
        }

        public Builder setAdListener(AdListener adListener) {
            this.adListener = adListener;
            return this;
        }

        public Builder setOnBackPressListener(OnBackPressListener onBackPressListener) {
            this.onBackPressListener = onBackPressListener;
            return this;
        }

        public Builder showReviewButton(boolean showReviewButton) {
            this.showReviewButton = showReviewButton;
            return this;
        }

        public TedAdmobDialog create() {
            return new TedAdmobDialog(this, R.style.TedAdmobDialog);
        }

    }
}
