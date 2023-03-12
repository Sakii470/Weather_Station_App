package com.example.platform.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.platform.Activities.PokazPolaActivity;
import com.example.platform.Models.RegionModel;
import com.example.platform.R;

import java.util.List;

public class RegionAdapter extends ArrayAdapter<RegionModel> {

    Context context;
    List<RegionModel> arrayListRegion;
    Animation animation;
    PokazPolaActivity pokazPolaActivity;
    static int i = 1;




    public RegionAdapter(@NonNull Context context, List<RegionModel> arrayListRegion) {
        super(context, R.layout.region_list_item1, arrayListRegion);
        this.context = context;
        this.arrayListRegion = arrayListRegion;
    }


    public static int getRandom(int min,int max){
        return (int) ((Math.random() * (max - min)) + min);
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate((R.layout.region_list_item1), null, true);
       // animation = AnimationUtils.loadAnimation(this.pokazPolaActivity,R.anim.animation1);
        TextView tvID = view.findViewById(R.id.textView);
        LinearLayout ll_bg = view.findViewById(R.id.ll_bg);
        int pos = position + 1;

        //tvID.setAnimation(animation);
        //int number = getRandom(1,8);
//        int number=2;
//        if(number == 1){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_1));
//        }
//        if(number == 2){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_2));
//        }
//        if(number == 3){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_3));
//        }
//        if(number == 4){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_4));
//        }
//        if(number == 5){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_5));
//        }
//        if(number == 6){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_6));
//        }
//        if(number == 7){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_7));
//        }
//        if(number == 8){
//            ll_bg.setBackground(ContextCompat.getDrawable(pokazPolaActivity, R.drawable.gradient_8));
//        }

        tvID.setText("Pole " + pos);
      //  tvID.setAnimation(animation);
        i++;

        return view;
    }
}
