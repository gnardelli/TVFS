


/*
 * Copyright (c) 2016. iElettronica.it
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


/*
 * Copyright (c) 2016. iElettronica.it
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package it.ielettronica.TVFS;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;



public class MainActivity extends FragmentActivity {

    //////////////////////////////////////////////////////////////
    private final static boolean mAmministrator=Boolean.TRUE;
    private final static int mGroupLevel=2;   // 0,1 or 2
    private final static boolean mBanner=Boolean.FALSE;
    /////////////////////////////////////////////////////////////

    ViewPager pager;
    PagerTabStrip tab_strp;
    public static MediaPlayer mPlayer;
    public final static String imageNullDefault = "https://static.parastorage.com/services/wixapps/2.477.0/javascript/wixapps/apps/blog/images/no-image-icon.png";
    public static Intent intent;
    public static  boolean createFirstTime = Boolean.FALSE;
    private static Context context;
    public static int channelValueMax;
    public static int channelValue;
    public static int volumeValue;
    public static int posLocalListBeforeExecuted;
    public static int posRemoteListBeforeExecuted;
    public static ma_pager_adapter mapager;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static boolean isAmministrator() {
        return mAmministrator;
    }
    public static boolean isWithBanner() {
        return mBanner;
    }

    public static int getGroupLevel() { return mGroupLevel;  }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static Context getAppContext() {
        return MainActivity.context;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        MainActivity.context = getApplicationContext();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_tab);
        mapager=new ma_pager_adapter(getSupportFragmentManager(),this);
        pager=(ViewPager)findViewById(R.id.pager);

        //pager.setScrollY(100);


        //// ROW USED FOR THE BANNER
        /////////////////////////////////////////////////////////
        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        /////////////////////////////////////////////////////////

        pager.setAdapter(mapager);
        tab_strp=(PagerTabStrip)findViewById(R.id.tab_strip);
        tab_strp.setTextColor(Color.WHITE);

       //pager.setOffscreenPageLimit(0);

        File folder = new File(Environment.getExternalStorageDirectory() + "/.Playlist");

        channelValueMax = 6;
        channelValue = 1;
        volumeValue = 90;


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((FragmentPagerAdapter) pager.getAdapter()).getItem(position);
                //Fragment fragment = ((FragmentStatePagerAdapter) pager.getAdapter()).getItem(position);
                if (position == 2 && fragment != null) {
                    fragment.onResume();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://it.ielettronica.TVFS/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                //Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://it.ielettronica.TVFS/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }




    /*
	 * AsyncTask that will download the xml file for us and store it locally.
	 * After the download is done we'll parse the local file.
	 */



}