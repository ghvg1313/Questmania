package com.akili.etc.triviacrashsaga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.view.WindowManager;
import android.widget.Toast;

import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.Quiz;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    private final int SLEEP_TIME = 500;
    public static final String CATEGORIES = "CATEGORYLIST";
    public static final String QUIZ = "QUIZQUESTIONS";
    private String categoryString;
    private Boolean checkNetworkConnectivity;


    private String identityID,identityName;


    public static HashMap<String, ArrayList<Quiz>> quizCategories = new HashMap<>();


    public static HashMap<String, Category> categoryquizMap = new HashMap<>();

    public static ArrayList<Quiz> quiz_new = new ArrayList<>();
    private ArrayList<Integer> categoryList = new ArrayList<>();
    public static HashMap<String, String> categoryMap = new HashMap<String, String>();
    //public static Context mContext;

    public static ArrayList<Quiz> quiz = new ArrayList<Quiz>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_splash);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        checkNetworkConnectivity = haveNetworkConnection();

        if (checkNetworkConnectivity) {

            categoryString = getJSON("http://128.2.239.212/akilibackend/api/start", 10000);
          /*  categoryString="[\n" +
                    "  {\n" +
                    "    \"id\": 1,\n" +
                    "    \"identity\": \"Doctor\"\n" +
                    "  }"+
                    "]";*/
            if (categoryString != null) {

                try {
                    JSONArray jsonArray = new JSONArray(categoryString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        identityID = json.optString("id").toString();
                        identityName = json.optString("identity").toString();
                        //Log.i("identityName>>", "" + identityName);
                        //Log.i("identityID>>", "" + identityID);
                        categoryMap.put(identityID, identityName);
                        saveQuiz(identityID, identityName);
                    }

                    SharedPreferences category_pref = getSharedPreferences(CATEGORIES, MODE_PRIVATE);
                    SharedPreferences.Editor editor_category = category_pref.edit();
                    for (String s : categoryMap.keySet()) {
                        editor_category.putString(s, categoryMap.get(s));
                    }
                    editor_category.commit();

                    loadQuizData(this);
                   // CenterController.controller().loadQuizData(this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else
                Toast.makeText(this, "Data Sync Failed.", Toast.LENGTH_LONG).show();
            //LoadPreferences();

        }
        LoadPreferences();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Login-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainMenuActivity.class);
                if (categoryList != null) {
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, SLEEP_TIME);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getJSON(String url, int timeout) {

        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                return sb.toString();

            }

        } catch (MalformedURLException ex) {
            // Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            // Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    private void LoadPreferences() {

        try {
            SharedPreferences category_pref = getSharedPreferences(CATEGORIES, Context.MODE_PRIVATE);
            HashMap<String, String> category_map = (HashMap<String, String>) category_pref.getAll();
            for (String s : category_map.keySet()) {
                String value = category_map.get(s);
                Log.i("category>>", "" + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Data Sync Failed.Check your Internet Connection  or Try Later", Toast.LENGTH_LONG).show();
        }
    }

    private void saveQuiz(String id_identity,String identity_name) {

        Log.i("id_identity>>", "" + id_identity);
        Log.i("identity_name>>", "" + identity_name);
        String string_URL = "http://128.2.239.212/akilibackend/api/questions/" + id_identity;
        /*String quiz_data="[{\n" +
                "  \"id\":1,\n" +
                "  \"question\":\"Who are you ?\",\n" +
                "  \"option0\" :\"Adarsh\",\n" +
                "  \"option1\" :\"Amy\",\n" +
                "  \"option2\" :\"Ladera\",\n" +
                "  \"option3\" :\"Ruchi\",\n" +
                "  \"answer\"  :0\n" +
                "  \n" +
                "}" +
                "]";*/
        String quiz_data = getJSON(string_URL, 10000);

        if(quiz_data == null) {
            quiz_data="NO_DATA";
        }
            SharedPreferences prefs = getSharedPreferences(identity_name.trim(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(identity_name, quiz_data);
            editor.commit();

    }
    public void loadQuizData(Context context)
    {

        SharedPreferences category_pref = context.getSharedPreferences(CATEGORIES,MODE_PRIVATE);
        HashMap<String, String> category_map = (HashMap<String, String>) category_pref.getAll();
        JSONArray quiz_json;
        ArrayList<Quiz> quiz = new ArrayList<Quiz>();

        String categoryName ;

        for (String s : category_map.keySet()) {
            categoryName = category_map.get(s);
            Log.i("categoryDDDDDD>>", "" + categoryName);

            SharedPreferences category = context.getSharedPreferences(categoryName.trim(),MODE_PRIVATE);
            String quiz_data = category.getString(categoryName.trim(),"");
            Log.i("quiz_dataDDDDDD>>", "" + quiz_data);

            if (quiz_data != null) {
                try {
                    quiz_json = new JSONArray(quiz_data);
                    for (int j = 0; j < quiz_json.length(); j++) {

                        JSONObject jsonObject = quiz_json.getJSONObject(j);

                            int id = Integer.parseInt(jsonObject.optString("id").toString());
                            String question = jsonObject.optString("question").toString();
                            String option0 = jsonObject.optString("option0").toString();
                            String option1 = jsonObject.optString("option1").toString();
                            String option2 = jsonObject.optString("option2").toString();
                            String option3 = jsonObject.optString("option3").toString();
                            int answer = Integer.parseInt(jsonObject.optString("answer").toString());
                            Log.i("question>>", "" + question);
                            quiz_new.add(new Quiz(question, option0, option1, option2, option3, answer));
                            //quizMap.put(id, new Quiz(question, option0, option1, option2, option3, answer));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //CenterController.quizCategories.put(categoryName.trim(), (ArrayList) quiz_new.clone());
            //CenterController.categoryMap.put(categoryName.trim(), new Category(categoryName.trim(), quiz));
        }


    }

}
