package com.sistempakar.bayes.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.model.Gejala;

import java.util.ArrayList;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class GejalaAdapter extends ArrayAdapter<Gejala> {

    private int layoutResourceId;
    private Context context;


    public GejalaAdapter(Context context, ArrayList<Gejala> items) {
        super(context, R.layout.item_gejala, items);
        this.layoutResourceId = R.layout.item_gejala;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Gejala gejala = getItem(position);

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

        holder.idGejala = (TextView) convertView.findViewById(R.id.id_gejala);
        holder.namaGejala = (TextView) convertView.findViewById(R.id.nama_gejala);
        holder.radioYa = (RadioButton) convertView.findViewById(R.id.radio_ya);
        holder.radioTidak = (RadioButton) convertView.findViewById(R.id.radio_tidak);

        holder.idGejala.setText(gejala.getIdGejala());
        holder.namaGejala.setText(gejala.getNamaGejala());


        if(holder.radioYa.isChecked()){
            gejala.setPilihGejala(gejala.getNamaGejala());
        }else {
            gejala.setPilihGejala("");
        }

        holder.radioYa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gejala.setPilihGejala(gejala.getNamaGejala());
                }
            }
        });

        holder.radioTidak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    gejala.setPilihGejala("");
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView idGejala;
        TextView namaGejala;
        RadioButton radioYa;
        RadioButton radioTidak;
    }


}