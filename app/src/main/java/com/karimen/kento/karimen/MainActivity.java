package com.karimen.kento.karimen;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


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
    }

    @Override
    public void onBackPressed() {
        int backStackCnt = getFragmentManager().getBackStackEntryCount();
        if (backStackCnt != 0) {
            getFragmentManager().popBackStack(); // BackStackに乗っているFragmentを戻す
        }



    }
}
