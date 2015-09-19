package com.karimen.kento.honmen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by Kento on 15/02/11.
 */
public class MyListAdapter extends BaseAdapter {
    ArrayList<Problem> results = new ArrayList<Problem>();
    private Context context;
    private LayoutInflater inflater;


    public MyListAdapter(Context context, ArrayList<Problem> data) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        results = data;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.image_result = (ImageView) convertView.findViewById(R.id.image_result_icon);
            holder.text_problem = (TextView) convertView.findViewById(R.id.text_problem);
            holder.text_q_num = (TextView) convertView.findViewById(R.id.text_q_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Problem result = results.get(position);
        AQuery aq = new AQuery(convertView);

        aq.id(holder.text_problem).text(result.getQuestion_text());
        aq.id(holder.text_q_num).text("Q." + String.valueOf(position + 1));

        if (String.valueOf(result.isUser_answer()) == String.valueOf(result.isCorrect_answer())) {
            Log.e("結果", "◯");
            aq.id(holder.image_result).image(R.drawable.maru);
        } else {
            Log.e("結果", "×");
            aq.id(holder.image_result).image(R.drawable.batu);
        }
        return convertView;
    }
}
