package com.akili.etc.triviacrashsaga.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.CategorySelectActivity;
import com.akili.etc.triviacrashsaga.Entity.RootCategoryButtonItem;
import com.akili.etc.triviacrashsaga.Entity.RootCategoryRowItem;
import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kangleif on 10/20/2015.
 */
public class RootCategoryAdapterItem extends ArrayAdapter<RootCategoryRowItem> {

    Context mContext;
    int layoutResourceId;
    RootCategoryRowItem data[] = null;

    public RootCategoryAdapterItem(Context mContext, int layoutResourceId, RootCategoryRowItem[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }


        ArrayList<RootCategoryButtonItem> rootCategoryButtonData = new ArrayList<>();
        //RootCategoryButtonItem[] rootCategoryButtonData = new RootCategoryButtonItem[3];

        if(position == 0) {
            //Interests
            for(AchievementSystem.CategoryType categoryType : CenterController.controller().playerInterests){
                rootCategoryButtonData.add(new RootCategoryButtonItem(AchievementSystem.categoryNameFromType(categoryType)));
            }
        } else if(position == 1){
            //Challenges
            ArrayList<String> names = (ArrayList)AchievementSystem.categoryNames.clone();
            Random randomizer = new Random();
            for(int i=0;i<AchievementSystem.categoryNames.size();i++){
                int resultIndex = randomizer.nextInt(names.size());
                rootCategoryButtonData.add(new RootCategoryButtonItem(names.get(resultIndex)));
                names.remove(resultIndex);
            }
        } else if(position == 2){
            //Other Interested
            for(String categoryName : AchievementSystem.categoryNames){
                boolean unique = true;
                for(AchievementSystem.CategoryType categoryType : CenterController.controller().playerInterests){
                    String name = AchievementSystem.categoryNameFromType(categoryType);
                    if(name.equals(categoryName)){
                        unique = false;
                        break;
                    }
                }
                if(unique){
                    rootCategoryButtonData.add(new RootCategoryButtonItem(categoryName));
                }
            }
        }

        RootCategoryButtonAdapterItem adapter = new RootCategoryButtonAdapterItem(getContext(), R.layout.list_horizontal_category_button, rootCategoryButtonData);
        if(position == 1){
            adapter.type= RootCategoryButtonAdapterItem.ButtonType.Challenges;
        }

        //Set each icon button
        TwoWayView horizontalListView = (TwoWayView) convertView.findViewById(R.id.lvItems);
        horizontalListView.setAdapter(adapter);

        RootCategoryRowItem item = data[position];
        //View backgroundView = convertView.findViewById(R.id.backgroundColor);
        TextView textView = (TextView) convertView.findViewById(R.id.textViewItem);
        textView.setText(item.itemName);

        return convertView;
    }

}
