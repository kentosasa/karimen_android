package com.karimen.kento.honmen;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItimonIttouFragment extends Fragment {

    final Gson gson = new Gson();
    final String url = "https://menkyo.herokuapp.com/api/get_all_problems";
    SharedPreferences pref;
    ArrayList<Integer> review_list;
    ArrayList<Problem> problems = new ArrayList<Problem>();
    String put_correct = "https://menkyo.herokuapp.com/api/put_correct?id=";
    String put_miss = "https://menkyo.herokuapp.com/api/put_miss?id=";
    AQuery aq;
    Context context;
    int question_num = 0;

    public ItimonIttouFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem, container, false);
        context = getActivity();
        pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        review_list = gson.fromJson(pref.getString("review_list", gson.toJson(new ArrayList<Integer>())), new TypeToken<ArrayList<Integer>>() {
        }.getType());

        aq = new AQuery(getActivity(), view);

        aq.id(R.id.text_title).text("一問一答");
        aq.ajax(url, JSONArray.class, this, "jsonArrayCallback");
        aq.id(R.id.layout_mogi).gone();
        aq.id(R.id.button_maru).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge(true);
            }
        });
        aq.id(R.id.button_batu).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge(false);
            }
        });

        return view;
    }

    public void judge(boolean click_button) {
        problems.get(question_num).setUser_answer(click_button);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (String.valueOf(problems.get(question_num).isCorrect_answer()) == String.valueOf(click_button)) {
            aq.ajax(put_correct + problems.get(question_num).getId(), String.class, this, "");
            dialog.setIcon(R.drawable.maru_50);
            dialog.setTitle("正解");

        } else {
            review_list.add(problems.get(question_num).getId());
            pref.edit().putString("review_list", gson.toJson(review_list)).commit();

            aq.ajax(put_miss + problems.get(question_num).getId(), String.class, this, "");
            dialog.setIcon(R.drawable.batu_50);
            dialog.setTitle("不正解");
        }
        dialog.setMessage(problems.get(question_num).getExplanation());
        dialog.setNegativeButton("次へ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (question_num < problems.size() - 1) question_num++;
                else question_num = 0;
                setQuestion();

            }
        });
        dialog.setPositiveButton("詳細", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ExplanationFragment explanationFragment = new ExplanationFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                bundle.putString("data", gson.toJson(problems.get(question_num)));
                ft.add(R.id.container, explanationFragment);
                explanationFragment.setArguments(bundle);
                ft.addToBackStack(null);
                ft.commit();

                if (question_num < problems.size() - 1) question_num++;
                else question_num = 0;
                setQuestion();
            }
        });
        dialog.show();
    }

    public void setQuestion() {
        String image_url = problems.get(question_num).getQuestion_image_url();
        if (image_url.length() > 5) {
            aq.id(R.id.image_problem).visible();
            aq.id(R.id.image_problem).image(image_url);
        } else {
            aq.id(R.id.image_problem).gone();
        }
        aq.id(R.id.text_problem).text(problems.get(question_num).question_text);
    }

    public void jsonArrayCallback(String url, JSONArray jsonArray, AjaxStatus status) {
        if (jsonArray != null) {
            for (int i = 0; jsonArray.length() > i; i++) {
                try {
                    Problem data = new Problem();
                    JSONObject raw = jsonArray.getJSONObject(i);
                    data.setId(raw.getInt("id"));
                    data.setQuestion_image_url(raw.getString("question_image_url"));
                    data.setQuestion_text(raw.getString("question_text"));
                    data.setExplanation(raw.getString("explanation"));
                    data.setCorrect_answer(raw.getBoolean("correct_answer"));
                    Log.e("Get Problem", "No." + data.getId());

                    aq.id(R.id.layout_mogi).visible();
                    aq.id(R.id.progressBar).gone();
                    problems.add(data);
                } catch (JSONException e) {
                    Log.e("JsonParse失敗", "残念");
                }
                ;
            }
            aq.id(R.id.text_problem).text(problems.get(question_num).question_text);
            aq.id(R.id.layout_mogi).visible();
            aq.id(R.id.progressBar).gone();
        } else {
            Toast.makeText(context, "ネットワークに接続できませんでした", Toast.LENGTH_SHORT).show();
            Log.e("NetWorkError", "Error!");
            Log.e("Status", status.getMessage());

        }
    }

}