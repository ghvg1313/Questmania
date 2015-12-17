package com.akili.etc.triviacrashsaga.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.CategorySelectActivity;
import com.akili.etc.triviacrashsaga.Entity.RootCategoryButtonItem;
import com.akili.etc.triviacrashsaga.Entity.RootCategoryRowItem;
import com.akili.etc.triviacrashsaga.PartyMode.ChallengePreTextActivity;
import com.akili.etc.triviacrashsaga.QuizActivity;
import com.akili.etc.triviacrashsaga.QuizStartActivity;
import com.akili.etc.triviacrashsaga.R;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import extension.ResourceTool;

/**
 * Created by kangleif on 10/20/2015.
 */
public class RootCategoryButtonAdapterItem extends ArrayAdapter<RootCategoryButtonItem> {

    public enum ButtonType{
        Recent,
        Challenges,
        Others
    }

    Context mContext;
    int layoutResourceId;
    ArrayList<RootCategoryButtonItem> data = new ArrayList<>();

    public ButtonType type = ButtonType.Recent;

    public RootCategoryButtonAdapterItem(Context mContext, int layoutResourceId, ArrayList data) {

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

        TextView textViewItem = (TextView) convertView.findViewById(R.id.textView);
        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);

        // object item based on the position
        RootCategoryButtonItem rootCategoryRowItem = data.get(position);

        textViewItem.setText(rootCategoryRowItem.itemName);

        if(type != ButtonType.Challenges) {
            if (rootCategoryRowItem.itemName.equals("Doctor")) {
                imageButton.setImageResource(R.drawable.id_doctor);
            } else if (rootCategoryRowItem.itemName.equals("Writer")) {
                imageButton.setImageResource(R.drawable.id_writer);
            } else if (rootCategoryRowItem.itemName.equals("Teacher")) {
                imageButton.setImageResource(R.drawable.id_teacher);
            } else if (rootCategoryRowItem.itemName.equals("Game Designer")) {
                imageButton.setImageResource(R.drawable.id_game_designer);
            } else if (rootCategoryRowItem.itemName.equals("Journalist")) {
                imageButton.setImageResource(R.drawable.id_journalist);
            } else if (rootCategoryRowItem.itemName.equals("Artist")) {
                imageButton.setImageResource(R.drawable.id_artist);
            }
        }else {
            if (rootCategoryRowItem.itemName.equals("Doctor")) {
                imageButton.setImageResource(R.drawable.root_challenge_doctor);
            } else if (rootCategoryRowItem.itemName.equals("Writer")) {
                imageButton.setImageResource(R.drawable.root_challenge_writer);
            } else if (rootCategoryRowItem.itemName.equals("Teacher")) {
                imageButton.setImageResource(R.drawable.root_challenge_teacher);
            } else if (rootCategoryRowItem.itemName.equals("Game Designer")) {
                imageButton.setImageResource(R.drawable.root_challenge_gamer);
            } else if (rootCategoryRowItem.itemName.equals("Journalist")) {
                imageButton.setImageResource(R.drawable.root_challenge_journalist);
            } else if (rootCategoryRowItem.itemName.equals("Artist")) {
                imageButton.setImageResource(R.drawable.root_challenge_artist);
            }
        }
        setImageButton(imageButton, rootCategoryRowItem.itemName);

        return convertView;
    }

    private void setImageButton (ImageButton button, final String categoryName) {
        if (type != ButtonType.Challenges) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), QuizStartActivity.class);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("level", "1");
                    getContext().startActivity(intent);
                }
            });
        }else{
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChallengePreTextActivity.class);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("challengeIndex", 0);
                    getContext().startActivity(intent);
                }
            });
        }
    }

}
