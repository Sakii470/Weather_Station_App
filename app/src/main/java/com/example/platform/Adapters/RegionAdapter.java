package com.example.platform.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.platform.Models.RegionModel;
import com.example.platform.R;

import java.util.List;

public class RegionAdapter extends ArrayAdapter<RegionModel> {

//variable declaration and initialization
    Context context;
    List<RegionModel> arrayListRegion;
    static int i = 0;

// Constructor RegionAdapter class
    public RegionAdapter(@NonNull Context context, List<RegionModel> arrayListRegion) {
        super(context, R.layout.region_list_item_regions, arrayListRegion);
        this.context = context;
        this.arrayListRegion = arrayListRegion;
    }

// Method that returns the actual view used as a row within the ListView. Possition variable defined which elament will be show.
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
// Define rows on ListView
        View view = LayoutInflater.from(parent.getContext()).inflate((R.layout.region_list_item_regions), null, true);
// Initialize variable.Views are find by Id (XML).
        TextView tvID = view.findViewById(R.id.textView);
// Increment possition variable.
        int pos = position + 1;
        tvID.setText("Pole " + pos);
        i++;
        return view;
    }
}
