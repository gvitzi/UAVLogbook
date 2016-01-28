package com.gilvitzi.uavlogbookpro.activity;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gilvitzi.uavlogbookpro.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gil on 27/01/2016.
 * Google AdMob Ads Manager, Manages One AdView
 */

public class GoogleAdMobFullScreenAd {

    private InterstitialAd mInterstitialAd = null;
    private Context context;
    private List<AdListener> mOnAdClosedListenersList;

    public GoogleAdMobFullScreenAd(Context context) {
        this.context = context;

        mOnAdClosedListenersList = new LinkedList<AdListener>();

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.ads_full_screen_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                onAdClosedEvent();
            }
        });

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = buildAdRequest();
        mInterstitialAd.loadAd(adRequest);
    }

    public void show(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            onAdClosedEvent();
        }
    }

    @NonNull
    private AdRequest buildAdRequest() {
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        String[] ids = context.getResources().getStringArray(R.array.ad_test_devices);
        for( String testDeviceID : ids) {
            adRequestBuilder.addTestDevice(testDeviceID);
        }

        AdRequest adRequest = adRequestBuilder.build();

        return adRequest;
    }

    public InterstitialAd getmInterstitialAd() {
        return mInterstitialAd;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void onAdClosedEvent()
    {
        requestNewInterstitial();

        for (AdListener listener : mOnAdClosedListenersList) {
            listener.onAdClosed();
        }
    }

    public void addAdListener(AdListener listener)
    {
        mOnAdClosedListenersList.add(listener);
    }

    public void removeAdListener(AdListener listener)
    {
        mOnAdClosedListenersList.remove(listener);
    }
}