package com.sopner.byakhya;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setBuiltInZoomControls(false);
        webView.addJavascriptInterface(new AndroidBridge(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // @A.M.MORSHED — HTML-এর ভেতরেও watermark inject করা হচ্ছে
                view.evaluateJavascript(
                    "(function(){" +
                    "  var existing = document.getElementById('am-morshed-wm');" +
                    "  if(existing) return;" +
                    "  var d = document.createElement('div');" +
                    "  d.id = 'am-morshed-wm';" +
                    "  d.innerText = '@A.M.MORSHED';" +
                    "  d.style.cssText = 'position:fixed;top:6px;right:8px;z-index:999999;" +
                    "    background:#cc0000;color:#fff;font-size:11px;font-weight:bold;" +
                    "    padding:2px 6px;border-radius:3px;font-family:monospace;" +
                    "    letter-spacing:0.5px;pointer-events:none;';" +
                    "  document.body.appendChild(d);" +
                    "})()",
                    null
                );
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }

    class AndroidBridge {
        @JavascriptInterface
        public void shareText(String text) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "শেয়ার করুন"));
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
