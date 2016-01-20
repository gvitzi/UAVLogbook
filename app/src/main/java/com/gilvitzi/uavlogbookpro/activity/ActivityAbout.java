
package com.gilvitzi.uavlogbookpro.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import com.gilvitzi.uavlogbookpro.R;
import com.google.android.gms.analytics.HitBuilders;

import java.lang.reflect.Field;

public class ActivityAbout extends DatabaseActivity {

	final static String LOG_TAG = "ActivityAbout";
	final static String screen_name = "About";

//    @Override
//    protected void onResume(){
//        super.onResume();
//    	Log.i(LOG_TAG, "Setting screen name: " + screen_name);
//    	mTracker.setScreenName("Image~" + screen_name);
//    	mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
//        //Google Analytics:
//
//        mTracker = AnalyticsApplication.getDefaultTracker(this);
        
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // Ignore
        }

        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void goToWhatsNew(View view)
    {
    	
    	Intent intent = new Intent(this, ActivityWhatsNew.class);
    	startActivity(intent);
    	
    	mTracker.send(new HitBuilders.EventBuilder()
        .setCategory("WhatsNew")
        .setAction("About Screen Whats New Button Clicked")
        .build());
    	
    	this.finish();
    }
}
