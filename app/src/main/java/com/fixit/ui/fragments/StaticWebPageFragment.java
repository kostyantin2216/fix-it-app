package com.fixit.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fixit.app.R;
import com.fixit.controllers.ActivityController;
import com.fixit.utils.Constants;

/**
 * Created by Kostyantin on 9/16/2017.
 */

public class StaticWebPageFragment extends BaseFragment<ActivityController> {

    public static StaticWebPageFragment newFragment(String title, String url) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_TITLE, title);
        args.putString(Constants.ARG_URL, url);

        StaticWebPageFragment fragment = new StaticWebPageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_static_web_page, container, false);

        initWebView((WebView) v.findViewById(R.id.web_view));

        return v;
    }

    private void initWebView(WebView webView) {
        Bundle args = getArguments();
        String title = args.getString(Constants.ARG_TITLE);
        String url = args.getString(Constants.ARG_URL);

        webView.setWebViewClient(new WebPageViewClient(TextUtils.isEmpty(title) ? url : title));
        webView.loadUrl(url);
    }

    private class WebPageViewClient extends WebViewClient {

        private final String pageTitle;

        WebPageViewClient(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showLoader(getString(R.string.loading_format, pageTitle), true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideLoader();
            super.onPageFinished(view, url);
        }

    }
}
