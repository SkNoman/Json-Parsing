package com.example.json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context applicationContext;
    int sample;
    List<DemoStudent> s;
    private LayoutInflater inflater;

    public CustomAdapter (Context applicationContext, int sample, List<DemoStudent> s) {

        this.applicationContext = applicationContext;
        this.sample = sample;
        this.s = s;

    }

    @Override
    public int getCount() {
        return s.size();
    }

    @Override
    public Object getItem(int position) {
        return s.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sample,viewGroup,false);
        }
        TextView name,id,country,hno;
        name = view.findViewById(R.id.name);
        id = view.findViewById(R.id.id);
        country = view.findViewById(R.id.country);
        hno = view.findViewById(R.id.hno);

        name.setText(s.get(position).getName());
        id.setText(s.get(position).getId());
        country.setText(s.get(position).getCountry());
        hno.setText(s.get(position).getCountry());
        return view;
    }
}
