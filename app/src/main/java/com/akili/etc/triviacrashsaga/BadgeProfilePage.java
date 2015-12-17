package com.akili.etc.triviacrashsaga;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Adapter.BadgeAdapter;
import com.akili.etc.triviacrashsaga.Entity.BadgeItem;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class BadgeProfilePage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private GridView gridView;
    private BadgeAdapter gridAdapter;
    private TextView achievementText;
    private ImageView achievementImage;

    public BadgeProfilePage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Prepare some dummy data for gridview
    private ArrayList<BadgeItem> getData() {
        return AchievementSystem.system().getAvailableBadges();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badge_profile_page, container, false);
        gridView = (GridView)view.findViewById(R.id.gridView);
        achievementText = (TextView)view.findViewById(R.id.achievementDescriptionText);
        achievementImage = (ImageView)view.findViewById(R.id.achievementImageView);
        gridAdapter = new BadgeAdapter(getContext(), R.layout.badge_layout, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setSelection(0);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                BadgeItem item = (BadgeItem) parent.getItemAtPosition(position);
                achievementImage.setImageBitmap(item.getImage());
                achievementText.setText(item.getTitle() + "\n" + item.getDescription());
            }
        });

        if(AchievementSystem.system().getAvailableBadges().size()>0){
            BadgeItem item = ((BadgeItem) AchievementSystem.system().getAvailableBadges().get(0));
            achievementImage.setImageBitmap(item.getImage());
            achievementText.setText(item.getTitle() + "\n" + item.getDescription());
        }

        return view;
    }

}
