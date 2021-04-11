package com.example.moinitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SMSAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<SMSData> sample;

    public SMSAdapter(Context context, ArrayList<SMSData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SMSData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listitem, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.warning);
        TextView sendNumber = (TextView) view.findViewById(R.id.sendNumber);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView content = (TextView) view.findViewById(R.id.content);

        imageView.setImageResource(sample.get(position).getWarning());
        sendNumber.setText(sample.get(position).getSendNumber());
        date.setText(sample.get(position).getDate());
        content.setText(sample.get(position).getContent());

        return view;
    }
}
