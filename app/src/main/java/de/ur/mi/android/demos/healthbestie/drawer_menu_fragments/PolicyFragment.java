package de.ur.mi.android.demos.healthbestie.drawer_menu_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import de.ur.mi.android.demos.healthbestie.R;


public class PolicyFragment extends Fragment {

    WebView policyWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_policy, container, false);
        policyWebView = (WebView) v.findViewById(R.id.policy_webView);
        policyWebView.getSettings().setJavaScriptEnabled(true);
        policyWebView.setWebViewClient(new WebViewClient());
        policyWebView.loadUrl("file:///android_asset/privacy_policy.html");
        return v;

    }
}