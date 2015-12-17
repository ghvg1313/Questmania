package com.akili.etc.triviacrashsaga.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Entity.PersonalRecord;
import com.akili.etc.triviacrashsaga.LatestProfilePage;
import com.akili.etc.triviacrashsaga.R;
import com.google.android.gms.plus.model.people.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kangleif on 11/18/2015.
 */
public class ProfileLatestAdapter extends ArrayAdapter<PersonalRecord> {

    Context mContext;
    int layoutResourceId;
    ArrayList<PersonalRecord> data = null;
    public LatestProfilePage.LatestType type = LatestProfilePage.LatestType.LATEST;

    public ProfileLatestAdapter(Context mContext, int layoutResourceId, ArrayList data) {
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

        PersonalRecord item = data.get(position);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateText);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionText);
        TextView extraContentTextView = (TextView)convertView.findViewById(R.id.moreContentText);

        if(item.extraString == null || item.extraString.length()<=0){
            extraContentTextView.setVisibility(View.INVISIBLE);
            //((LinearLayout)extraContentTextView.getParent()).removeView(extraContentTextView);
        }else{
            extraContentTextView.setVisibility(View.VISIBLE);
            extraContentTextView.setText(item.extraString);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        dateTextView.setText(sdf.format(item.date));
        descriptionTextView.setText(item.recordContent);


        return convertView;
    }
}
