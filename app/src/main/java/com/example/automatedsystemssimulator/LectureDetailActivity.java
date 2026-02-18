package com.example.automatedsystemssimulator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Lecture;
import com.example.automatedsystemssimulator.utils.ProgressController;
import com.google.android.material.button.MaterialButton;

public class LectureDetailActivity extends AppCompatActivity {

    private static final String EXTRA_LECTURE_ID = "lecture_id";

    private WebView webView;
    private FrameLayout fullscreenContainer;
    private ScrollView scrollView;
    private LinearLayout layoutVideo;
    private LinearLayout contentContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    public static void start(Context context, Lecture lecture) {
        Intent intent = new Intent(context, LectureDetailActivity.class);
        intent.putExtra(EXTRA_LECTURE_ID, lecture.getId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MaterialButton btnGoToPractice = findViewById(R.id.btnGoToPractice);
        btnGoToPractice.setOnClickListener(v -> {
            Intent intent = new Intent(LectureDetailActivity.this, TestTopicsActivity.class);
            startActivity(intent);
        });

        int lectureId = getIntent().getIntExtra(EXTRA_LECTURE_ID, -1);
        Lecture lecture = DataProvider.getLectureById(lectureId);
        if (lecture == null) {
            finish();
            return;
        }

        ProgressController pm = new ProgressController(this);
        pm.markLectureRead(lectureId);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvContent = findViewById(R.id.tvContent);
        tvTitle.setText(lecture.getTitle());
        tvContent.setText(lecture.getContent());

        webView = findViewById(R.id.webViewVideo);
        fullscreenContainer = findViewById(R.id.fullscreenContainer);
        scrollView = findViewById(R.id.scrollView);
        layoutVideo = findViewById(R.id.layoutVideo);
        contentContainer = findViewById(R.id.contentContainer);

        String videoUrl = lecture.getVideoUrl();
        if (videoUrl != null && !videoUrl.isEmpty()) {
            layoutVideo.setVisibility(LinearLayout.VISIBLE);
            setupWebView(webView, videoUrl);
        } else {
            layoutVideo.setVisibility(LinearLayout.GONE);
        }

        CardView cardImportant = findViewById(R.id.cardImportantInfo);
        TextView tvImportant = findViewById(R.id.tvImportantInfo);
        String important = lecture.getImportantInfo();
        if (important != null && !important.isEmpty()) {
            tvImportant.setText(important);
            cardImportant.setVisibility(CardView.VISIBLE);
        } else {
            cardImportant.setVisibility(CardView.GONE);
        }

        if (lectureId == 1) {
            addComponentsBlock();
        }
    }

    private void addComponentsBlock() {
        TextView tvTitleComponents = new TextView(this);
        tvTitleComponents.setText("🏭 Основные компоненты АСУ:");
        tvTitleComponents.setTextSize(18);
        tvTitleComponents.setTypeface(tvTitleComponents.getTypeface(), android.graphics.Typeface.BOLD);
        tvTitleComponents.setTextColor(getColor(R.color.gray_900));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.topMargin = dpToPx(8);
        titleParams.bottomMargin = dpToPx(12);
        tvTitleComponents.setLayoutParams(titleParams);

        LinearLayout componentsContainer = new LinearLayout(this);
        componentsContainer.setOrientation(LinearLayout.VERTICAL);
        componentsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) componentsContainer.getLayoutParams()).bottomMargin = dpToPx(24);

        componentsContainer.addView(createComponentItem("1", "Датчики и сенсоры"));
        componentsContainer.addView(createComponentItem("2", "Контроллеры (ПЛК)"));
        componentsContainer.addView(createComponentItem("3", "SCADA-системы"));

        int videoIndex = -1;
        for (int i = 0; i < contentContainer.getChildCount(); i++) {
            if (contentContainer.getChildAt(i) == layoutVideo) {
                videoIndex = i;
                break;
            }
        }

        if (videoIndex == -1) {

            int contentCardIndex = -1;
            for (int i = 0; i < contentContainer.getChildCount(); i++) {
                if (contentContainer.getChildAt(i) instanceof CardView) {
                    CardView card = (CardView) contentContainer.getChildAt(i);
                    if (card.findViewById(R.id.tvContent) != null) {
                        contentCardIndex = i;
                        break;
                    }
                }
            }
            if (contentCardIndex != -1) {
                contentContainer.addView(tvTitleComponents, contentCardIndex + 1);
                contentContainer.addView(componentsContainer, contentCardIndex + 2);
            } else {
                int importantIndex = -1;
                for (int i = 0; i < contentContainer.getChildCount(); i++) {
                    if (contentContainer.getChildAt(i) == findViewById(R.id.cardImportantInfo)) {
                        importantIndex = i;
                        break;
                    }
                }
                if (importantIndex != -1) {
                    contentContainer.addView(tvTitleComponents, importantIndex);
                    contentContainer.addView(componentsContainer, importantIndex + 1);
                } else {
                    contentContainer.addView(tvTitleComponents);
                    contentContainer.addView(componentsContainer);
                }
            }
        } else {
            contentContainer.addView(tvTitleComponents, videoIndex + 1);
            contentContainer.addView(componentsContainer, videoIndex + 2);
        }
    }

    private LinearLayout createComponentItem(String number, String text) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) itemLayout.getLayoutParams()).bottomMargin = dpToPx(12);

        // Кружок с цифрой
        TextView tvNumber = new TextView(this);
        tvNumber.setText(number);
        tvNumber.setGravity(android.view.Gravity.CENTER);
        tvNumber.setTextSize(14);
        tvNumber.setTypeface(tvNumber.getTypeface(), android.graphics.Typeface.BOLD);
        tvNumber.setTextColor(getColor(R.color.purple_700));
        tvNumber.setBackgroundResource(R.drawable.bg_circle_light);
        LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(dpToPx(30), dpToPx(30));
        numberParams.setMarginEnd(dpToPx(12));
        tvNumber.setLayoutParams(numberParams);

        TextView tvText = new TextView(this);
        tvText.setText(text);
        tvText.setTextSize(16);
        tvText.setTextColor(getColor(R.color.gray_800));
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        tvText.setLayoutParams(textParams);

        itemLayout.addView(tvNumber);
        itemLayout.addView(tvText);

        return itemLayout;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void setupWebView(WebView webView, String url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(url);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (customView != null) {
                callback.onCustomViewHidden();
                return;
            }

            customView = view;
            customViewCallback = callback;

            fullscreenContainer.setVisibility(View.VISIBLE);
            fullscreenContainer.addView(customView, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            scrollView.setVisibility(View.GONE);
        }

        @Override
        public void onHideCustomView() {
            if (customView == null) return;

            customView.setVisibility(View.GONE);
            fullscreenContainer.removeView(customView);
            customView = null;
            customViewCallback.onCustomViewHidden();
            customViewCallback = null;

            fullscreenContainer.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            if (webView.getWebChromeClient() != null) {
                webView.getWebChromeClient().onHideCustomView();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}