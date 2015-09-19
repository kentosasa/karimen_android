package com.karimen.kento.honmen;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.google.gson.Gson;


public class ExplanationFragment extends Fragment {


    public ExplanationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explanation, container, false);
        final Context context = getActivity();
        String data = getArguments().getString("data");
        Gson gson = new Gson();
        Problem problem = gson.fromJson(data, Problem.class);
        AQuery aq = new AQuery(getActivity(), view);

        String image_url = problem.getQuestion_image_url();
        if (image_url.length() > 5) {
            aq.id(R.id.image_explanation).visible();
            aq.id(R.id.image_explanation).image(image_url);
        } else {
            aq.id(R.id.image_explanation).gone();
        }
        aq.id(R.id.text_problem).text(problem.getQuestion_text());
        aq.id(R.id.text_explanation).text(problem.getExplanation());
        if (problem.isUser_answer()) aq.id(R.id.text_answer).text("あなたの回答　◯");
        else aq.id(R.id.text_answer).text("あなたの回答　×");
        if (problem.isCorrect_answer()) aq.id(R.id.text_correct_answer).text("答え　◯");
        else aq.id(R.id.text_correct_answer).text("答え　×");

        aq.id(R.id.button_back).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack(); // BackStackに乗っているFragmentを戻す
            }
        });

        return view;
    }

}
