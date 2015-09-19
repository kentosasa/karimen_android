package com.karimen.kento.honmen;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Kento on 15/02/14.
 */
public class ReviewFragment extends Fragment {

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        final Context context = getActivity();
        AQuery aq = new AQuery(getActivity(), view);
        aq.id(R.id.button_retry).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RetryFragment retryFragment = new RetryFragment();
                ft.add(R.id.container, retryFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        aq.id(R.id.button_review_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ReviewListFragment reviewListFragment = new ReviewListFragment();
                ft.add(R.id.container, reviewListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        aq.id(R.id.button_reset_data).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("履歴を削除しますか");
                dialog.setNegativeButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        pref.edit().putString("review_list", gson.toJson(new ArrayList<Integer>())).commit();
                    }
                });
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        aq.id(R.id.button_back).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TopFragment topFragment = new TopFragment();
                ft.add(R.id.container, topFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return view;
    }

}
