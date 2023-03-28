package com.example.platform.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.platform.Models.PomiarModel;
import com.example.platform.R;

import java.util.List;

public class PomiarAdapter extends ArrayAdapter<PomiarModel> {

    Context context;
    List<PomiarModel> pomiar_list;

    public PomiarAdapter(Context context, List<PomiarModel> pomiar_list) {
        super(context, R.layout.pomiar_list_item_data, pomiar_list);
        this.context = (Context) context;
        this.pomiar_list = pomiar_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate((R.layout.pomiar_list_item_data), null, true);
        TextView tvDataa = view.findViewById(R.id.dataa_tv);
        TextView tvNazwa_Sensora = view.findViewById(R.id.nazwa_sensora_tv);
        TextView tvWilgotnosc = view.findViewById(R.id.wilgotnosc_tv);
        tvDataa.setText(pomiar_list.get(position).getDataa());
        tvNazwa_Sensora.setText(pomiar_list.get(position).getNazwa_sensora());
        tvWilgotnosc.setText(pomiar_list.get(position).getWilgotnosc());

        return view;
    }

}
