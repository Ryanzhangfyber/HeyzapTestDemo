package rzhang.heyzaproid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.heyzap.internal.Logger;
import com.heyzap.sdk.ads.BannerAdView;
import com.heyzap.sdk.ads.HeyzapAds;
import com.heyzap.sdk.ads.IncentivizedAd;
import com.heyzap.sdk.ads.InterstitialAd;
import com.heyzap.sdk.ads.NativeAd;
import com.heyzap.sdk.ads.NativeListener;
import com.heyzap.sdk.ads.NativeAd.Image;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import com.heyzap.sdk.mediation.adapter.UnityadsAdapter;
public class MainActivity extends AppCompatActivity {

    public static String PUBLISHER_ID = "fc67f3e9044474b00c46427c70c4778f";
    private Button bTestUI;
    private Button bRequest;
    private Button bShow;
    private Button RequestInter;
    private Button ShowInter;
    private Button bBanner;
    private Button LoadNative;
    private BannerAdView bannerAdView;
    final private NativeAd nativeAd = new NativeAd();
    private JSONObject remoteData = HeyzapAds.getRemoteData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HeyzapAds.setGdprConsent(false,this);
        //Log.d("TAG","This line is setting GDPR as false-1");

        if(HeyzapAds.hasStarted()) {
            Log.d("TAG", "----------->Before initialized.<----------");
            IncentivizedAd.fetch();
        }
        Log.d("TAG", "----------->before initialized.<----------");

        //initialize Heyzap SDK
        HeyzapAds.start(PUBLISHER_ID,MainActivity.this);

        //Add facebook test device
        AdSettings.addTestDevice("267d12b82e6bcba44fc08a6a97fe9e71");

        //Add the dangerous permissions manually
        PermissionUtils.verifyStoragePermissions(this);

        if(HeyzapAds.hasStarted()) {
            Log.d("TAG", "----------->Heyzap SDK has been initialized.(After)<----------");
            IncentivizedAd.fetch();
        }

        Logger.setDebugLogging(true);

        //HeyzapAds.setGdprConsent(true,this);
        //Log.d("TAG","This line is setting GDPR as true-1");

        final String currency = remoteData.optString("currency","Gems");
        final int amount = remoteData.optInt("amount");
        Log.d("----currency: "+currency,"----amount: "+amount);

        //fetch banner with option
        bannerAdView = new BannerAdView(MainActivity.this);
        HeyzapAds.BannerOptions bannerOptions = bannerAdView.getBannerOptions();
        bannerOptions.setAdmobBannerSize(HeyzapAds.CreativeSize.BANNER_HEIGHT_90);

        //create banner wrapper
        FrameLayout bannerWrapper = (FrameLayout)findViewById(R.id.banner_frame);
        bannerWrapper.addView(bannerAdView);

        //Add HeyzapAds network callback
        HeyzapAds.setNetworkCallbackListener(new HeyzapAds.NetworkCallbackListener() {
            @Override
            public void onNetworkCallback(String network, String event) {
                System.out.println(network + "  ->TAG<-  " + event);
            }
        });

        //Add listener to InterstialAd
        InterstitialAd.setOnStatusListener(new HeyzapAds.OnStatusListener() {
            @Override
            public void onShow(String s) {
                Log.d("TAG","TAG---Interstitial onShow-------->"+InterIsReady()+" (InterIsReady())");
            }

            @Override
            public void onClick(String s) {
                Log.d("TAG","TAG---Interstitial Click--------");
            }

            @Override
            public void onHide(String s) {
                Log.d("TAG","-------Interstitial Hide-------");
            }

            @Override
            public void onFailedToShow(String s) {
                Log.d("TAG","--------->Interstitial Fail To Show<-------------");
            }

            @Override
            public void onAvailable(String s) {
                Log.d("TAG","--------->Interstitial Available<-------------");
            }

            @Override
            public void onFailedToFetch(String s) {
                Log.d("TAG","--------->Interstitial Fail To Fetch<-------------");
            }

            @Override
            public void onAudioStarted() {

            }

            @Override
            public void onAudioFinished() {

            }
        });

        //Add listener to Incentivized
        IncentivizedAd.setOnStatusListener(new HeyzapAds.OnStatusListener() {
            @Override
            public void onShow(String s) {
                //HeyzapAds.setGdprConsent(true,MainActivity.this);
                //Log.d("TAG","This line is setting GDPR as true-3");
                Log.d("TAG","TAG---RV onShow-------->"+RVisReady()+" (RVisReady())");
            }

            @Override
            public void onClick(String s) {
                Log.d("TAG","TAG---RV Click--------");
            }

            @Override
            public void onHide(String s) {
                Log.d("TAG","-------RV Hide-------");
            }

            @Override
            public void onFailedToShow(String s) {
                Log.d("TAG","--------->RV Fail To Show<-------------");
            }

            @Override
            public void onAvailable(String s) {
                Log.d("TAG","--------->RV Available<-------------");
            }

            @Override
            public void onFailedToFetch(String s) {
                Log.d("TAG","--------->RV Fail To Fetch<-------------");
            }

            @Override
            public void onAudioStarted() {

            }

            @Override
            public void onAudioFinished() {

            }
        });

        IncentivizedAd.setOnIncentiveResultListener(new HeyzapAds.OnIncentiveResultListener() {
            @Override
            public void onComplete(String s) {
                Log.d("TAG","--------->RV Complete<-------------");
            }

            @Override
            public void onIncomplete(String s) {
                Log.d("TAG","--------->RV InComplete<-------------");
            }
        });

        //Add listener to Banner
        bannerAdView.setBannerListener(new HeyzapAds.BannerListener() {
            @Override
            public void onAdError(BannerAdView bannerAdView, HeyzapAds.BannerError bannerError) {
                Log.d("TAG","--------------------->BannerError<----------------------");
            }

            @Override
            public void onAdLoaded(BannerAdView bannerAdView) {
                Log.d("TAG","--------------------->BannerLoaded<----------------------");
            }

            @Override
            public void onAdClicked(BannerAdView bannerAdView) {
                Log.d("TAG","--------------------->BannerClicked<----------------------");
            }
        });

        //Add listener to NativeAds
        nativeAd.setListener(new NativeListener() {
            @Override
            public void onError(HeyzapAds.NativeError nativeError) {
                Log.d("TAG","TAG---NativeAdError-------->" + nativeError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(NativeAd nativeAd) {
                setupNativeAdView(nativeAd);
                Log.d("TAG","TAG---NativeAdLoaded-------->");
            }

            @Override
            public void onAdClicked(NativeAd nativeAd) {

            }

            @Override
            public void onAdShown(NativeAd nativeAd) {
                Log.d("TAG","TAG---NativeAdShow-------->");
            }
        });

        //Add listener to Admob NativeAds
        nativeAd.setAdmobListener(new NativeAd.AdmobListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAd nativeAd, NativeAppInstallAd nativeAppInstallAd) {
                //setupAppInstallAd(nativeAd,nativeAppInstallAd);
                Log.d("TAG","TAG---Admob Native onAppInstallAdLoaded-------->");
            }

            @Override
            public void onContentAdLoaded(NativeAd nativeAd, NativeContentAd nativeContentAd) {
                //setupContentAd(nativeAd,nativeContentAd);
                Log.d("TAG","TAG---Admob Native onContentAdLoaded-------->");
            }
        });

        //create TestUI button
        bTestUI = (Button)findViewById(R.id.button_TestUI);
        bTestUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeyzapAds.startTestActivity(MainActivity.this);
            }
        });

        //create RV Request button
        bRequest = (Button)findViewById(R.id.button_request);
        bRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncentivizedAd.fetch();
                Log.d("TAG","TAG---RV Fetch-------->"+RVisReady()+" (RVisReady())");
            }
        });

        //create RV Show button
        bShow = (Button)findViewById(R.id.button_show);
        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RVisReady()) {
                    IncentivizedAd.display(MainActivity.this);
                    Log.d("TAG","TAG---RV Show-------->"+RVisReady()+" (RVisReady())");
                } else {
                    Log.d("Tag", "TAG---RV ShowFail-------->" + RVisReady()+" (RVisReady())");
                }
            }
        });

        //create Interstitial Request button
        RequestInter = (Button)findViewById(R.id.button_r_Interstitial);
        RequestInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterstitialAd.fetch();
                Log.d("TAG","TAG---Interstitial Fetch-------->"+InterIsReady()+" (InterIsReady())");
            }
        });

        //create Interstitial Show button
        ShowInter = (Button)findViewById(R.id.button_s_Interstitial);
        ShowInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InterIsReady()) {
                    InterstitialAd.display(MainActivity.this);
                    Log.d("TAG","TAG---Interstitial Show-------->"+InterIsReady()+" (InterIsReady())");
                } else{
                    Log.d("Tag", "TAG---Interstitial ShowFail-------->" + InterIsReady()+" (InterIsReady())");
                }
            }
        });

        bBanner = (Button)findViewById(R.id.button_banner);
        bBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerAdView.load();
            }
        });

        LoadNative = (Button)findViewById(R.id.button_loadNativeAds);
        LoadNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeAd.load();
            }
        });
    }

    protected void onResume(){
        bannerAdView.destroy();
        //HeyzapAds.setGdprConsent(true,this);
        //Log.d("TAG","This line is setting GDPR as true-2");
        super.onResume();
    }

    protected void onDestroy() {
        bannerAdView.destroy();
        super.onDestroy();
    }

    private boolean RVisReady(){
        return IncentivizedAd.isAvailable();
    }

    private boolean InterIsReady(){
        return InterstitialAd.isAvailable();
    }

    private void setupNativeAdView(final NativeAd nativeAd){
        final String title = nativeAd.getTitle();
        final String body = nativeAd.getBody();
        final String callToAction = nativeAd.getCallToAction();
        final Image icon = nativeAd.getIcon();
        final Image coverImage = nativeAd.getCoverImage();

        final RelativeLayout container = findViewById(R.id.native_ad_container);
        final LinearLayout mainUI = findViewById(R.id.main_ui);

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView titleView = (TextView)container.findViewById(R.id.title);
                TextView bodyView = (TextView) container.findViewById(R.id.body);
                titleView.setText(title);
                bodyView.setText(body);

                String iconUrl = icon.getUrl();
                int height = icon.getHeight();
                int width = icon.getWidth();
                ImageView iconImageView =
                        (ImageView) container.findViewById(R.id.icon_image);
                Picasso.with(MainActivity.this).load(iconUrl).into(iconImageView);

                String contentUrl = coverImage.getUrl();
                ImageView contentImageView = (ImageView)container.findViewById(R.id.content_image);
                Picasso.with(MainActivity.this).load(contentUrl).into(contentImageView);

                Button button = (Button) container.findViewById(R.id.button_CTA);
                button.setText(callToAction);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nativeAd.doClick(v);
                    }
                });

                nativeAd.registerView(container);

                mainUI.setVisibility(View.INVISIBLE);
                container.setVisibility(View.VISIBLE);
                nativeAd.doImpression();
            }
        });
    }
}
