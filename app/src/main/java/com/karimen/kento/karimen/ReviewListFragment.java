package com.karimen.kento.karimen;

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
import android.widget.AdapterView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewListFragment extends Fragment {

    final Gson gson = new Gson();
    SharedPreferences pref;
    ArrayList<Integer> review_list;
    ArrayList<Problem> results = new ArrayList<Problem>();
    Context context;
    AQuery aq;

    public ReviewListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        context = getActivity();

        pref = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        review_list = gson.fromJson(pref.getString("review_list", gson.toJson(new ArrayList<Integer>())), new TypeToken<ArrayList<Integer>>(){}.getType());

        String url = "https://menkyo.herokuapp.com/api/get_review_list?list=";
        for (int i = 0; i < review_list.size(); i++){
            url = url + review_list.get(i) + ",";
        }
        aq = new AQuery(getActivity(), view);
        aq.ajax(url, JSONArray.class, this, "jsonArrayCallback");


        aq.id(R.id.button_back).clicked(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ReviewFragment reviewFragment = new ReviewFragment();
                ft.remove(ReviewListFragment.this);
                ft.add(R.id.container, reviewFragment);
                ft.commit();
            }
        });
        return view;
    }


    public void jsonArrayCallback(String url, JSONArray jsonArray, AjaxStatus status){
        Log.e("Callback", "Callback");
        Log.e("URL", url);
        if (jsonArray != null){
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
                    results.add(data);
                } catch (JSONException e) {
                    Log.e("JsonParse失敗", "残念");
                };
            }

            final MyListAdapter adapter = new MyListAdapter(getActivity(), results);
            aq.id(R.id.list_result).adapter(adapter).itemClicked(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setNegativeButton("リストから削除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            review_list.remove(position);
                            pref.edit().putString("review_list", gson.toJson(review_list)).commit();
                            results.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    dialog.setPositiveButton("解説", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                    dialog.show();
                }
            });

//            aq.id(R.id.layout_mogi).visible();
//            aq.id(R.id.progressBar).gone();
        }else{
            Toast.makeText(context, "ネットワークに接続できませんでした", Toast.LENGTH_SHORT).show();
            Log.e("NetWorkError", "Error!");
            Log.e("Status", status.getMessage());

        }
    }

}
