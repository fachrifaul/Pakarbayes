package com.sistempakar.bayes.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.model.Rules;

import java.util.ArrayList;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class RulesAdapter extends ArrayAdapter<Rules> {

    private int layoutResourceId;
    private Context context;


    public RulesAdapter(Context context, ArrayList<Rules> items) {
        super(context, R.layout.item_rules, items);
        this.layoutResourceId = R.layout.item_rules;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Rules Rules = getItem(position);

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idRules = (TextView) convertView.findViewById(R.id.id_rules);
        holder.namaRules = (TextView) convertView.findViewById(R.id.nama_rules);
        holder.hasilRules = (TextView) convertView.findViewById(R.id.hasil_rules);

        holder.idRules.setText(Rules.getIdRules());
        holder.namaRules.setText(Rules.getNamaRules());
        holder.hasilRules.setText(Rules.getPilihRules());


        return convertView;
    }

    static class ViewHolder {
        TextView idRules;
        TextView namaRules;
        TextView hasilRules;
    }


}