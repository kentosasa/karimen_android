package com.karimen.kento.honmen;

import android.app.Activity;
import android.os.Bundle;

import net.nend.android.NendAdInterstitial;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new TopFragment())
                    .commit();
        }
        NendAdInterstitial.loadAd(getApplicationContext(), "519a87ded748846617f0bf2528b6c448e6220829", 338068);

    }

    @Override
    public void onBackPressed() {
        int backStackCnt = getFragmentManager().getBackStackEntryCount();
        if (backStackCnt != 0) {
            getFragmentManager().popBackStack(); // BackStackに乗っているFragmentを戻す
        }


    }
}
