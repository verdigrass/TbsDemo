package com.tbs.demo

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tencent.smtt.sdk.CookieSyncManager
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class KotlinActivity : AppCompatActivity() {

    var mContext : Context? = null;
    var mWebView : WebView? = null;

    var root : ViewGroup? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this;

        var layoutInflater = this.getLayoutInflater();
        root = layoutInflater.inflate(R.layout.activity_kotlin, null) as? ViewGroup;

        var layout : ViewGroup? = root?.findViewById(R.id.content) as? ViewGroup;
        setupWebView(layout);

        setContentView(root)
    }

    class TbsWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(webview: WebView?, url: String?): Boolean {
            return false;
        }

    }

    fun setupWebView(layout : ViewGroup?) {

        mWebView = WebView(mContext);
        mWebView?.setWebViewClient(TbsWebViewClient());

        layout?.addView(mWebView);

        window.setFormat(PixelFormat.TRANSLUCENT);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= 11) {
            window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        val webSetting = mWebView?.getSettings()
        webSetting?.javaScriptEnabled = true
        webSetting?.allowFileAccess = true
        webSetting?.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting?.setSupportZoom(true)
        webSetting?.builtInZoomControls = true
        webSetting?.useWideViewPort = true
        webSetting?.setSupportMultipleWindows(false)
        webSetting?.loadWithOverviewMode = true
        webSetting?.setAppCacheEnabled(true)
        webSetting?.databaseEnabled = true
        webSetting?.domStorageEnabled = true
        webSetting?.setGeolocationEnabled(true)
        webSetting?.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting?.setAppCachePath(this.getDir("appcache", 0).path)
        webSetting?.databasePath = this.getDir("databases", 0).path
        webSetting?.setGeolocationDatabasePath(this.getDir("geolocation", 0).path)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting?.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting?.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSetting?.displayZoomControls = false
        webSetting?.mediaPlaybackRequiresUserGesture = false

        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().sync()


        val mUrl : EditText? = root?.findViewById(R.id.editUrl2) as? EditText;
        var mGo : Button? = root?.findViewById(R.id.btnGo2) as? Button;

        Log.e("Kotlin", "mUrl: " + mUrl + ", mGo: " + mGo);

        mGo?.setOnClickListener(View.OnClickListener { view ->


            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mUrl?.getWindowToken(), 0)


            var url : String? = mUrl?.text.toString();
            mWebView?.loadUrl(url);

            mWebView?.requestFocus();
        })

        mUrl?.setOnFocusChangeListener(View.OnFocusChangeListener { view : View?, hasFocus : Boolean ->

            Log.e("Kotlin", "OnFocusChangeListener, hasFocus: " + hasFocus);

            if (hasFocus) {
                mGo?.visibility = View.VISIBLE;

                mGo?.setText("Enter")
                mGo?.setTextColor(0X6F0000CD)
            } else {
                mGo?.visibility = View.GONE;

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.getWindowToken(), 0)
            }

        });

    }

    override fun onResume() {
        super.onResume()

        mWebView?.loadUrl("https://www.bing.com");
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {

        if (mWebView?.canGoBack() ?: false) {
            mWebView?.goBack();
        } else {
            super.onBackPressed()
        }
    }

}
