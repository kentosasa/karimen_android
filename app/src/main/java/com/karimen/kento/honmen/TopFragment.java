package com.karimen.kento.honmen;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class TopFragment extends Fragment {


    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        final Context context = getActivity();
        final AQuery aq = new AQuery(getActivity(), view);

        aq.id(R.id.button_mogi).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MogiFragment mogiFragment = new MogiFragment();
                ft.add(R.id.container, mogiFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        aq.id(R.id.button_itimonittou).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ItimonIttouFragment itimonIttouFragment = new ItimonIttouFragment();
                ft.add(R.id.container, itimonIttouFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        aq.id(R.id.button_review).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                SharedPreferences pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
                ArrayList<Integer> review_list = gson.fromJson(pref.getString("review_list", gson.toJson(new ArrayList<Integer>())), new TypeToken<ArrayList<Integer>>() {
                }.getType());

                if (review_list.size() > 0) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ReviewFragment reviewFragment = new ReviewFragment();
                    ft.add(R.id.container, reviewFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    Toast.makeText(context, "復習リストに問題がありません。", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
