package com.akili.etc.triviacrashsaga;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class CategorySelectActivity extends AppCompatActivity {

    private ImageButton category1, category2, category3, category4, challengeButton,backButton;
    private ImageButton[] categoryButtons;
    private TextView categoryTag1, categoryTag2, categoryTag3, categoryTag4, challengeTag;
    private TextView categoryTextView;
    private TextView[] categoryTags;

    private View backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_category_select);
        category1= (ImageButton) findViewById(R.id.categoryButton1);
        category2= (ImageButton) findViewById(R.id.categoryButton2);
        category3= (ImageButton) findViewById(R.id.categoryButton3);
        category4= (ImageButton) findViewById(R.id.categoryButton4);

        backButton =(ImageButton) findViewById(R.id.back_button);

        challengeButton = (ImageButton) findViewById(R.id.challengeButton);
        challengeButton.setVisibility(View.INVISIBLE);

        categoryTextView = (TextView) findViewById(R.id.categoryText);

        categoryTag1 = (TextView) findViewById(R.id.categoryName1);
        categoryTag2 = (TextView) findViewById(R.id.categoryName2);
        categoryTag3 = (TextView) findViewById(R.id.categoryName3);
        categoryTag4 = (TextView) findViewById(R.id.categoryName4);
        challengeTag = (TextView) findViewById(R.id.challengeTag);

        challengeTag.setVisibility(View.INVISIBLE);

        backgroundView = findViewById(R.id.backgroundView);

        categoryButtons = new ImageButton[]{category1, category2, category3, category4};
        categoryTags = new TextView[]{categoryTag1, categoryTag2, categoryTag3, categoryTag4};

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryTextView.setText(extras.get("RootType").toString());
            String type = extras.get("RootType").toString();

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            if(type.equals("Doctor")){
                backgroundView.setBackground(getResources().getDrawable(R.drawable.background_doctor));
                categoryTextView.setBackgroundColor(getResources().getColor(R.color.category_doctor_color));
                categoryTag1.setText("Doctor");
                categoryButtons[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(MainMenuActivity.modeSelect=='S') {
                        Intent intent = new Intent(CategorySelectActivity.this, QuizStartActivity.class);
                        intent.putExtra("categoryName", "Doctor");
                        intent.putExtra("level", "1");
                        startActivity(intent);
                    }
                     if(MainMenuActivity.modeSelect=='C') {
                         Intent intent = new Intent(CategorySelectActivity.this, QuizChallengeActivity.class);
                         intent.putExtra("categoryName", "Doctor");
                         intent.putExtra("level", "1");
                         startActivity(intent);
                     }
                 }
                });

                challengeButton.setVisibility(View.VISIBLE);
                challengeTag.setVisibility(View.VISIBLE);
                challengeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategorySelectActivity.this, SoloChallenge.class);
                        startActivity(intent);
                    }
                });
                for(int i=1; i<4;i++){
                    categoryButtons[i].setBackgroundResource(R.drawable.quiz_start_empty);
                }
                category1.setImageDrawable(getResources().getDrawable(R.drawable.quiz_start_doctor_amoeba));
            } else if(type.equals("Teacher")){
                backgroundView.setBackground(getResources().getDrawable(R.drawable.background_teacher));
                categoryTextView.setBackgroundColor(getResources().getColor(R.color.category_teacher_color));
                categoryTag1.setText("Teacher");
                categoryButtons[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainMenuActivity.modeSelect == 'S') {
                            Intent intent = new Intent(CategorySelectActivity.this, QuizStartActivity.class);
                            intent.putExtra("categoryName", "Teacher");
                            intent.putExtra("level", "1");
                            startActivity(intent);
                        }
                        if (MainMenuActivity.modeSelect == 'C') {
                            Intent intent = new Intent(CategorySelectActivity.this, QuizChallengeActivity.class);
                            intent.putExtra("categoryName", "Teacher");
                            intent.putExtra("level", "1");
                            startActivity(intent);
                        }
                    }
                });
                for(int i=1; i<4;i++){
                    categoryButtons[i].setBackgroundResource(R.drawable.quiz_start_teacher_empty);
                }
                category1.setImageDrawable(getResources().getDrawable(R.drawable.quiz_start_teacher_assortment));
            } else if(type.equals("Writer")){
                backgroundView.setBackground(getResources().getDrawable(R.drawable.background_writer));
                categoryTextView.setBackgroundColor(getResources().getColor(R.color.category_writer_color));
                categoryTag1.setText("Writer");
                categoryButtons[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainMenuActivity.modeSelect == 'S') {
                            Intent intent = new Intent(CategorySelectActivity.this, QuizStartActivity.class);
                            intent.putExtra("categoryName", "Writer");
                            intent.putExtra("level", "1");
                            startActivity(intent);
                        }
                        if (MainMenuActivity.modeSelect == 'C') {
                            Intent intent = new Intent(CategorySelectActivity.this, QuizChallengeActivity.class);
                            intent.putExtra("categoryName", "Writer");
                            intent.putExtra("level", "1");
                            startActivity(intent);
                        }
                    }
                });
                for(int i=1; i<4;i++){
                    categoryButtons[i].setBackgroundResource(R.drawable.quiz_start_writer_empty);
                }
                category1.setImageDrawable(getResources().getDrawable(R.drawable.quiz_start_writer_whatinastory));
            }
        }

//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        int index = 0;
//        for(final HashMap.Entry<String, ArrayList<Quiz>> entry : CenterController.controller().quizCategories.entrySet()) {
//            categoryTags[index].setText(entry.getKey());
//            categoryButtons[index].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CategorySelectActivity.this, QuizStartActivity.class);
//                    intent.putExtra("categoryName", entry.getKey());
//                    intent.putExtra("level", "1");
//                    startActivity(intent);
//                }
//            });
//            index++;
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_select, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CategorySelectActivity.this, RootCategoryActivity.class));
    }   

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
