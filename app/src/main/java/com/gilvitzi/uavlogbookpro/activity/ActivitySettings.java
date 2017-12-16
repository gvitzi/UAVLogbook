package com.gilvitzi.uavlogbookpro.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.gilvitzi.uavlogbookpro.R;
import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;

public class ActivitySettings extends AnalyticsActivity {
    private static final String LOG_TAG = "ActivitySettings";
    private Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Switch hoursFractionFormatSwitch;
    Button removeAdsBtn;
    EditText removeAdsCodeEditText;
    LinearLayout removeAdsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        settings = getSharedPreferences("UserInfo", 0);
        editor = settings.edit();

        hoursFractionFormatSwitch = (Switch) findViewById(R.id.switch_hours_fraction_format);
        removeAdsBtn = (Button) findViewById(R.id.remove_ads_button);
        removeAdsCodeEditText = (EditText) findViewById(R.id.remove_ads_coupon_code);
        removeAdsLayout = (LinearLayout) findViewById(R.id.remove_ads_layout);

        initValues();
        setChangeListeners();
    }

    private void setChangeListeners() {

        hoursFractionFormatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hours_fraction_format", isChecked);
                editor.commit();
                sendSettingsAnalyticsEvent("hours_fraction_format", String.valueOf(isChecked));
            }
        });

        removeAdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = removeAdsCodeEditText.getText().toString();
                String[] correctCodes = getResources().getStringArray(R.array.coupon_codes);
                boolean found = false;

                for (String correctCode : correctCodes) {
                    if (code.equals(correctCode)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    editor.putBoolean("show_ads", false);
                    editor.commit();
                    disableAdsCouponLayout();

                    String message = "Ads successfully removed!";
                    Log.e(LOG_TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                    sendSettingsAnalyticsEvent("ads_removed", code);
                } else {
                    String message = "Wrong Coupon Code";
                    Log.e(LOG_TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void disableAdsCouponLayout() {
        removeAdsCodeEditText.setEnabled(false);
        removeAdsBtn.setEnabled(false);
        removeAdsLayout.setEnabled(false);
    }

    private void initValues() {
        Boolean hoursFractionFormat = settings.getBoolean("hours_fraction_format", false);
        hoursFractionFormatSwitch.setChecked(hoursFractionFormat);
        boolean adsAlreadyRemoved = !settings.getBoolean("show_ads", true);

        if (adsAlreadyRemoved) {
            disableAdsCouponLayout();
        }
    }

    private void sendSettingsAnalyticsEvent(String setting, String newValue) {
        // send analytics event
        String category = getResources().getString(R.string.analytics_event_settings_changed);
        String action = String.format("%1$s set to %2$s", setting, newValue);

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }
}