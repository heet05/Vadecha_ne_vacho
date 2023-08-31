package com.heetVadecha.vadecha_news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

public class Detailed extends AppCompatActivity {

    TextView tvTitle,tvSource,tvTime,tvDesc;
    ImageView imageView,iv;
    WebView webView;
    ProgressBar loader;

    private FrameLayout mAdView;
    AdView  adView;
    AdRequest adRequest;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getSupportActionBar().hide();

        tvTitle = findViewById(R.id.tvTitle);
        tvSource = findViewById(R.id.tvSource);
        tvTime = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDesc);
        iv = findViewById(R.id.back_button);
        imageView = findViewById(R.id.imageView);
        mAdView = findViewById(R.id.adView);
        webView = findViewById(R.id.webView);

        loader = findViewById(R.id.webViewLoader);
        loader.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String source = intent.getStringExtra("source");
        String time = intent.getStringExtra("time");
        String desc = intent.getStringExtra("desc");
        String imageUrl = intent.getStringExtra("imageUrl");
        String url = intent.getStringExtra("url");


        tvTitle.setText(title);
        tvSource.setText(source);
        tvTime.setText(time);
        tvDesc.setText(desc);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = new AdView(this);
        mAdView.addView(adView);

        adView.setAdUnitId("ca-app-pub-6788818386275854/9343170224");
        loadBanner();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInterstitialAd();
              finish();
            }
        });



        Picasso.get().load(imageUrl).into(imageView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if (webView.isShown()){
            loader.setVisibility(View.INVISIBLE);
        }
    }
    public void loadInterstitialAd(){
        adRequest = new AdRequest.Builder()
                .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        InterstitialAd.load(getApplicationContext(), "ca-app-pub-6788818386275854/6176674732", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // this method is called when ad is loaded in that case we are displaying our ad.
                interstitialAd.show(Detailed.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // this method is called when we get any error
                Toast.makeText(Detailed.this, "Fail to load ad..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadBanner() {
        adRequest = new AdRequest.Builder()
                .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        AdSize adSize = getAdSize();
        // Set the adaptive ad size to the ad view.
        adView.setAdSize(adSize);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
    private AdSize getAdSize() {
        //Determine the screen width to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        //you can also pass your selected width here in dp
        int adWidth = (int) (widthPixels / density);

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
