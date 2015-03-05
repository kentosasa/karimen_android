package com.karimen.kento.karimen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class MogiResultFragment extends Fragment{


    public MogiResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mogi_result, container, false);
        String data = getArguments().getString("key");
        Gson gson = new Gson();
        final ArrayList<Problem> results = gson.fromJson(data, new TypeToken<ArrayList<Problem>>(){}.getType());
        AQuery aq = new AQuery(getActivity(), view);
        MyListAdapter adapter = new MyListAdapter(getActivity(), results);

        int score = 0;
        for (int i = 0; results.size() > i; i++){
            Problem result = results.get(i);
            if (result.isUser_answer() == result.isCorrect_answer()){
                score++;
            }
        }
        if (score >= 45){
            aq.id(R.id.text_pass_of_fail).text("合格 ").textColor(R.color.Red);
            aq.id(R.id.text_score).text(score + "/50" );
        }else{
            aq.id(R.id.text_pass_of_fail).text("不合格 ").textColor(R.color.Blue);
            aq.id(R.id.text_score).text(score + "/50" );
        }
        aq.id(R.id.list_result).adapter(adapter).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ExplanationFragment explanationFragment = new ExplanationFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                bundle.putString("data", gson.toJson(results.get(position)));
                ft.add(R.id.container, explanationFragment);
                explanationFragment.setArguments(bundle);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        aq.id(R.id.button_back).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TopFragment topFragment = new TopFragment();
                ft.remove(MogiResultFragment.this);
                ft.add(R.id.container, topFragment);
                ft.commit();
            }
        });

        return view;
    }

}
