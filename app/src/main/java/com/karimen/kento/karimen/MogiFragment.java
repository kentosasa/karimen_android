package com.karimen.kento.karimen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class MogiFragment extends Fragment{

    final String url = "https://menkyo.herokuapp.com/api/get_problems?honmen=1&count=50";
    final Gson gson = new Gson();
    String put_correct = "https://menkyo.herokuapp.com/api/put_correct?id=";
    String put_miss = "https://menkyo.herokuapp.com/api/put_miss?id=";
    ArrayList<Problem> problems = new ArrayList<Problem>();
    AQuery aq;
    SharedPreferences pref;
    ArrayList<Integer> review_list;
    Context context;
    View view;
    int question_num = 0;

    public MogiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_problem, container, false);
        context = getActivity();

        pref = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        review_list = gson.fromJson(pref.getString("review_list", gson.toJson(new ArrayList<Integer>())), new TypeToken<ArrayList<Integer>>(){}.getType());


        aq = new AQuery(getActivity(), view);
        aq.id(R.id.text_title).text("模擬試験");

        aq.ajax(url, JSONArray.class, this, "jsonArrayCallback");
        aq.id(R.id.layout_mogi).gone();


        aq.id(R.id.button_maru).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judege(true);
            }
        });
        aq.id(R.id.button_batu).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judege(false);
            }
        });
        return view;
    }

    public void judege(boolean clicked_button){
        problems.get(question_num).setUser_answer(clicked_button);
        if (String.valueOf(problems.get(question_num).isCorrect_answer()) == String.valueOf(clicked_button)) aq.ajax(put_correct+problems.get(question_num).getId(), String.class, this, "");
        else{
            review_list.add(problems.get(question_num).getId());
            pref.edit().putString("review_list", gson.toJson(review_list)).commit();
            aq.ajax(put_miss+problems.get(question_num).getId(), String.class, this, "");
        }
        if (question_num == problems.size()-1){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putString("key", gson.toJson(problems));
            MogiResultFragment mogiResultFragment = new MogiResultFragment();
            mogiResultFragment.setArguments(bundle);
            ft.remove(MogiFragment.this);
            ft.add(R.id.container, mogiResultFragment);
            ft.addToBackStack(null);
            ft.commit();

        }else{
            question_num++;
            setQuestion();
        }
    }
    public void setQuestion(){
        String image_url = problems.get(question_num).getQuestion_image_url();
        if (image_url.length() > 5){
            Log.e("url", image_url);
            aq.id(R.id.image_problem).visible();
            aq.id(R.id.image_problem).image(image_url, true, true, 0, 0, new BitmapAjaxCallback(){
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    super.callback(url, iv, bm, status);
                    Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            aq.id(R.id.image_problem).gone();
            aq.id(R.id.image_problem).visible();
            aq.id(R.id.image_problem).image("http://www5b.biglobe.ne.jp/~nobusann/777/honmen/hyou012g.gif", false, false, 0, 0, new BitmapAjaxCallback(){
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    super.callback(url, iv, bm, status);
                    Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        aq.id(R.id.text_problem).text("Q." + (question_num+1) + "\n" + problems.get(question_num).question_text);
    }

    public void jsonArrayCallback(String url, JSONArray jsonArray, AjaxStatus status){
        Log.e("Callback", "Callback");
        Log.e("URL", url);
        if (jsonArray != null){
            for (int i = 0; jsonArray.length() > i; i++) {
                try {
                    Problem problem = new Problem();
                    JSONObject raw = jsonArray.getJSONObject(i);
                    problem.setId(raw.getInt("id"));
                    problem.setQuestion_image_url(raw.getString("question_image_url"));
                    problem.setQuestion_text(raw.getString("question_text"));
                    problem.setExplanation(raw.getString("explanation"));
                    problem.setCorrect_answer(raw.getBoolean("correct_answer"));
                    problems.add(problem);
                    Log.e("Get Problem", "No."+problem.getId());
                } catch (JSONException e) {
                    Log.e("JsonParse失敗", "残念");
                };
            }
            aq.id(R.id.layout_mogi).visible();
            aq.id(R.id.progressBar).gone();
            setQuestion();
            Log.e("Get JsonArray", "Success!");
        }else{
            Toast.makeText(context, "ネットワークに接続できませんでした", Toast.LENGTH_SHORT).show();
            Log.e("NetWorkError", "Error!");
            Log.e("Status", status.getMessage());
        }
    }
}