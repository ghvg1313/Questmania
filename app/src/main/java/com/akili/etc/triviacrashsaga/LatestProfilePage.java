package com.akili.etc.triviacrashsaga;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Adapter.ProfileLatestAdapter;
import com.akili.etc.triviacrashsaga.Entity.PersonalRecord;
import com.akili.etc.triviacrashsaga.Singleton.ProfileController;

import java.util.ArrayList;

public class LatestProfilePage extends Fragment {

    private ListView listView;

    public enum LatestType{
        LATEST,
        JOURNAL
    }

    public LatestType type = LatestType.LATEST;

    public LatestProfilePage(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latest_profile_page, container, false);
        listView = (ListView)view.findViewById(R.id.listView);

        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        if(this.type == LatestType.LATEST){
            titleTextView.setText("Your latest Achievements!");
        }else{
            titleTextView.setText("Your challenge journals");
        }

        ProfileLatestAdapter adapter = new ProfileLatestAdapter(this.getContext(), R.layout.latest_layout, getData());
        listView.setAdapter(adapter);

        return view;
    }

    private ArrayList getData()
    {
        if(type == LatestType.LATEST)
            return ProfileController.latestRecords;
        else
            return ProfileController.journalist;
    }


}
