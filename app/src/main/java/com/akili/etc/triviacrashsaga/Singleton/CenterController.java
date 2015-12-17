package com.akili.etc.triviacrashsaga.Singleton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akili.etc.triviacrashsaga.Adapter.ProfileLatestAdapter;
import com.akili.etc.triviacrashsaga.Entity.Category;
import com.akili.etc.triviacrashsaga.Entity.Challenge;
import com.akili.etc.triviacrashsaga.Entity.PartyGame;
import com.akili.etc.triviacrashsaga.Entity.PersonalRecord;
import com.akili.etc.triviacrashsaga.Entity.Quiz;
import com.akili.etc.triviacrashsaga.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import extension.ResourceTool;

/**
 * Singleton that manage the game data
 * User profile, any data that go across the activities should be put here
 * Created by kangleif on 9/21/2015.
 */

public class CenterController {

    public static final int scorePerLevel = 100;

    public HashMap<String, ArrayList<Quiz>> quizCategories = new HashMap<>();

    public HashMap<String, ArrayList<Challenge>> personalChallenge = new HashMap<>();

    public HashMap<String, Category> categoryMap = new HashMap<>();

    public HashMap<String, Integer> playerScores = new HashMap<>();

    public ArrayList<AchievementSystem.CategoryType> playerInterests = new ArrayList();

    private ArrayList<AchievementSystem.CategoryType> categoryHistory = new ArrayList<>();

    private static CenterController ourInstance = new CenterController();

    public static boolean startShown = false;
    public static boolean triviaShown = false;
    public static boolean challengeshown = false;
    public static boolean firstOnboardingShown = false;
    public static boolean loaded = false;

    public String userName;

    public PartyGame partyGame;

    public static CenterController controller() {
        return ourInstance;
    }

    private CenterController() {
        loadData();
        loadQuiz();
        loadChallenge();
    }

    public void saveData()
    {
        Hawk.chain()
                .put("Name", "Kanye West")
                .put("QuizCategories", quizCategories)
                .put("CategoryMap", categoryMap)
                .put("PlayerScores", playerScores)
                .put("Onboarding", firstOnboardingShown)
                .put("PlayerInterests", playerInterests)
                .put("CategoryHistory", categoryHistory)
                .put("Journalist", ProfileController.controller().journalist)
                .put("LatestRecords", ProfileController.controller().latestRecords)
                .commit();
    }

    public void getData()
    {
        firstOnboardingShown = Hawk.get("Onboarding", firstOnboardingShown);
        userName = Hawk.get("Name");
        quizCategories = Hawk.get("QuizCategories",quizCategories);
        categoryMap = Hawk.get("CategoryMap",categoryMap);
        playerScores = Hawk.get("PlayerScores",playerScores);
        playerInterests = Hawk.get("PlayerInterests", playerInterests);
        categoryHistory = Hawk.get("CategoryHistory", categoryHistory);
        ProfileController.controller().journalist =  Hawk.get("Journalist", ProfileController.controller().journalist);
        ProfileController.controller().latestRecords =  Hawk.get("LatestRecords", ProfileController.controller().latestRecords);
    }


    public void initalizeDatabase(Context c)
    {
        Hawk.init(c)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSqliteStorage(c))
                .setLogLevel(LogLevel.NONE)
                .build();
        getData();
    }

    public int getLevel(AchievementSystem.CategoryType category){
        return playerScores.get(AchievementSystem.categoryNameFromType(category))/scorePerLevel;
    }

    public String getCaller(Context c, AchievementSystem.CategoryType category){
        int level = playerScores.get(AchievementSystem.categoryNameFromType(category))/scorePerLevel;
        TypedArray callers = c.getResources().obtainTypedArray(R.array.doctor_callers);
        if(category == AchievementSystem.CategoryType.DOCTOR){
            callers = c.getResources().obtainTypedArray(R.array.doctor_callers);
        } else if(category == AchievementSystem.CategoryType.WRITER){
            callers = c.getResources().obtainTypedArray(R.array.writer_callers);
        } else if(category == AchievementSystem.CategoryType.TEACHER){
            callers = c.getResources().obtainTypedArray(R.array.teacher_callers);
        } else if(category == AchievementSystem.CategoryType.GAMER){
            callers = c.getResources().obtainTypedArray(R.array.gamer_callers);
        } else if(category == AchievementSystem.CategoryType.ARTIST){
            callers = c.getResources().obtainTypedArray(R.array.artist_callers);
        } else if(category == AchievementSystem.CategoryType.JOURNALIST){
            callers = c.getResources().obtainTypedArray(R.array.journalist_callers);
        }

        if(level <= 1){
            return callers.getString(0);
        } else if(level <=3){
            return callers.getString(1);
        } else if(level <=5){
            return callers.getString(2);
        } else if(level <=7){
            return callers.getString(3);
        } else if(level <=9){
            return callers.getString(4);
        }
        return "Molecule Master";
    }

    //Everything related to achievement should be called here, because wee need a context and we don't want to disturb player when quiz is on
    //when user scores, check everything related to achievement
    public void score(Context context, String categoryName, int score, int rightCount){
        int oldScore = playerScores.get(categoryName);
        playerScores.put(categoryName, oldScore + score);

        if((oldScore+score)/100 != (oldScore)/100) {
            ProfileController.addToLatest("Leveled up in " + categoryName);
            showDialogWithImage(context, "Leveled up in "+categoryName, "You have reached level " + ((oldScore + score) / 100), ((BitmapDrawable)ResourceTool.getIdentityIcon(context, AchievementSystem.categoryTypeFromName(categoryName))).getBitmap());
        }


        if(score>=100){
            AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.SCORE100, categoryName);
        }else if(score>=80 && score <100){
            AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.SCORE80, categoryName);
        }

        if((oldScore+score)/scorePerLevel != oldScore/scorePerLevel && (oldScore+score)/scorePerLevel == 1) {
            AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.LEVEL1, categoryName);
        }

        if((oldScore+score)/scorePerLevel >= 5){
            AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.LEVELMAX, categoryName);
        }

        categoryMap.get(categoryName).rightCount += rightCount;
        for(String name : AchievementSystem.categoryNames){
            if(categoryMap.get(name).rightCount>=30){
                AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.QUESTION30, name);
            }else if(categoryMap.get(name).rightCount>=20){
                AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.QUESTION20, name);
            }else if(categoryMap.get(name).rightCount>=10){
                AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.QUESTION10, name);
            }
        }

        saveData();
    }

    static private void showDialog(Context c, View view){
        final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(c);
        dialogBuilder
                .withTitle("Congratulations!")
                .setCustomView(view, c)
                .withButton1Text("OK")
                .withDialogColor("#ffffff")
                .withMessageColor("#000000")
                .withTitleColor("#000000")
                .withButtonDrawable(R.color.navigation_color)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

    static public void showDialogWithTextOnly(Context c, String title) {
        LayoutInflater li=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView=li.inflate(R.layout.badge_alert, null);
        ((LinearLayout)((TextView)alertView.findViewById(R.id.badgeTitle)).getParent()).removeView(alertView.findViewById(R.id.badgeTitle));
        ((TextView)alertView.findViewById(R.id.badgeDescription)).setText(title);
        ((LinearLayout)((ImageView)alertView.findViewById(R.id.badgeIcon)).getParent()).removeView(alertView.findViewById(R.id.badgeIcon));
        showDialog(c, alertView);
    }

    static public void showDialogWithImage(Context c, String title, String message, Bitmap image){
        LayoutInflater li=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView=li.inflate(R.layout.badge_alert, null);
        ((TextView)alertView.findViewById(R.id.badgeTitle)).setText(title);
        ((TextView)alertView.findViewById(R.id.badgeDescription)).setText(message);
        ((ImageView)alertView.findViewById(R.id.badgeIcon)).setImageBitmap(image);
        showDialog(c, alertView);
    }

    public void completeChallenge(Context context, String categoryName){
        AchievementSystem.system().achievementUnlocked(context, AchievementSystem.AchievementType.CHALLENGE1, categoryName);
    }

    public void changeCategoryInterest(AchievementSystem.CategoryType category){
        if(playerInterests.contains(category)) {
            playerInterests.remove(category);
        }else {
            playerInterests.add(0, category);
        }
        saveData();
    }

    public void addCategoryInterest(AchievementSystem.CategoryType category){
        if(playerInterests.contains(category)) {
            playerInterests.remove(category);
            playerInterests.add(0, category);
        }else {
            playerInterests.add(0, category);
        }
        saveData();
    }

    private void loadData()
    {
        playerScores.put("Doctor", 0);
        playerScores.put("Writer", 0);
        playerScores.put("Teacher", 0);
        playerScores.put("Game Designer", 0);
        playerScores.put("Journalist", 0);
        playerScores.put("Artist", 0);
    }

    private void loadQuiz()
    {
        ArrayList<Quiz> quiz = new ArrayList<Quiz>();
        quiz.add(new Quiz("Which body of water is the longest and most important river in England, and runs through the heart of London?","Thames","Eden","Trent","Severn",0, "Assortment"));
        quiz.add(new Quiz("What would you measure with an odometer?","Pungency of odour","Frequency of vibration","Distance Travelled","Depth of water",2, "Assortment"));
        quiz.add(new Quiz("Who invented the World Wide Web?","John Logie Baird","Tim Berners-Lee ","Vint Cerf","Alexander Graham bell",1, "Assortment"));
        quiz.add(new Quiz("Which planet has largest known volcano in the solar system?","Mercury","Mars","Earth","Venus",1, "Assortment"));
        quiz.add(new Quiz("Identify from the following the method by which heat is transferred from a higher temperature to a lower temperature.","Convention","Translation","Commutation","Convection",3, "Assortment"));
        quiz.add(new Quiz("Identify the New England poet who wrote these lines…”Whose woods these are? I think I know. His house is in the village, though…”","William Blake","John Keats","Robert Frost ","William Wordsworth",2, "Assortment"));
        quiz.add(new Quiz("Identify the noble gas, discovered in 1898 from the residue of liquid air, that has an atomic number of 54.","Radon","Krypton","Xenon","Argon",2, "Assortment"));
        quiz.add(new Quiz("What two countries border the Dead Sea?","Israel and Egypt","Israel and Jordan ","Jordan and Saudi Arabia","Lebanon and Syria",1, "Assortment"));
        quiz.add(new Quiz("Namibia became a colony of what European nation in 1890, under the name South-West Africa?","Germany","Great Britain","The Netherlands","Portugal",0, "Assortment"));
        quiz.add(new Quiz("Which country contains the most languages?","Papua New Guinea","China","Australia","Jamaica",0, "Assortment"));
        quiz.add(new Quiz("Identify the verb, beginning with the letter ―b, that means to give a gift to someone through one‘s last will and testament. ","beguile","bequeath","benefit","bolster",1, "Assortment"));
        quiz.add(new Quiz("How many times louder is a sound of 40 decibels compared to a sound of 10 decibels? ","4","100","1000","4000",2, "Assortment"));
        quiz.add(new Quiz("Identify the six-letter noun that refers to protection provided to political refugees by a state or country with diplomatic immunity.","asylum","policy","safety","pardon",0, "Assortment"));
        quiz.add(new Quiz("Cosmology is the scientific discipline that studies the origin and development of the what? ","Evolution","Beauty","Civilization","The Universe",3, "Assortment"));
        quiz.add(new Quiz("For more than forty years, this building was the tallest in the world. Identify this famous building, which is still appreciated today for its Art Deco style, and still lays claim to the title of the tallest building in New York City.","Flat Iron Building","Empire State Building","Sears Tower","World Trade Center",1, "Assortment"));
        quiz.add(new Quiz("What is the Latin name for the constellation otherwise known as ―The Twins? ","Capricorn","Sagittarius","Gemini","Pisces",2, "Assortment"));
        quiz.add(new Quiz("Which word describes the animals native to a region? ","flora","fauna","falal","fey",1, "Assortment"));
        quiz.add(new Quiz("Which creature is the only natural enemy of the great white shark? ","Tiger Shark","Orca","Stingray","Clown Fish",1, "Assortment"));
        quiz.add(new Quiz("In Greek and Roman mythology, it was known as the nectar of the gods. Name it. ","water of river styx","ambrosia","wine","grape leaves",1, "Assortment"));
        quiz.add(new Quiz("With which sport would you associate nocks, quivers, and broadheads? ","curling","wrestling","cricket","archery",3, "Assortment"));
        quiz.add(new Quiz("Africa is home to the world‘s heaviest insect—a beetle that can weigh up to 100 grams. What is the beetle’s called?","Atlas","Prometheus","Hercules","Goliath ",3, "Assortment"));
        quiz.add(new Quiz("Mount Kosciusko is the tallest mountain in which country? ","New Zealand","Australia","South Africa","Poland",1, "Assortment"));
        quiz.add(new Quiz("Identify the Russian composer of the Christmas favorite The Nutcracker.","Rachmaninoff","Bach","Beethoven","Tchaikovsky",3, "Assortment"));
        quiz.add(new Quiz("Which substance, found in bogs and swamps, can be described as ―unfinished coal? ","anthracite","bitumen","peat","petroleum",2, "Assortment"));
        quiz.add(new Quiz("Who was the first U.S. President to be inaugurated in the White House?","George Washington","John Adams","Thomas Jefferson","James Madison",2, "Assortment"));
        quiz.add(new Quiz("What is nephrology the study of?","Dead bodies","kidneys","clouds","trees",1, "Assortment"));
        quiz.add(new Quiz("Identify the verb tense used in the following sentence: She will have been there for four days by Friday.","future perfect ","simple future","future continuous","compound future",0, "Assortment"));
        quiz.add(new Quiz("In addition to blood type, what other aspect of blood chemistry must be identified prior to a transfusion?","iron levels","white blood cell count","rhesus factor","plasma levels",2, "Assortment"));
        quiz.add(new Quiz("Which Greek ancient created “Oedipus at Colonus”, “Antigone” and “Electra”?", "Plato", "Sophocles", "Aristotle", "Homer", 1, "Assortment"));
        quizCategories.put("Teacher", (ArrayList) quiz.clone());
        categoryMap.put("Teacher", new Category("Teacher", quiz));

        quiz.clear();
        quiz.add(new Quiz("Which of these is not an element of a story?", "Setting", "Plot", "Point of View", "Prose", 3, "What's in a Story"));
        quiz.add(new Quiz("Which of these is the Setting?", "The meaning of the story", "The action in the story", "The people in the story", "Where and when a story takes place", 3, "What's in a Story"));
        quiz.add(new Quiz("Which of these is part of the Plot?","Red Riding Hood","The Woods Red Riding Hood walks through","Don’t talk to strangers","Red Riding Hood meets the wolf",3, "What's in a Story"));
        quiz.add(new Quiz("What are the two types of Conflict?","Internal and External","True and False","Rising and Falling","Passive and Aggressive", 0, "What's in a Story"));
        quiz.add(new Quiz("What is the central character in a story called?","Narrator","Antagonist","Protagonist","Star",2, "What's in a Story"));
        quiz.add(new Quiz("What is the character who opposes the central character called?","Narrator","Antagonist","Protagonist","Enemy",1, "What's in a Story"));
        quiz.add(new Quiz("If a story is told using the words I and Me, what is the point of view?","First Person","Second Person","Third Person","Fourth Person",0, "What's in a Story"));
        quiz.add(new Quiz("What is the theme of the story?","The feeling ","The moral","The action ","The visuals",1, "What's in a Story"));
        quiz.add(new Quiz("Which of these is the theme?","Red Riding Hood","The Woods Red Riding Hood walks through","Don’t talk to strangers","Red Riding Hood meets the wolf",2, "What's in a Story"));
        quiz.add(new Quiz("Rising Action is a part of what Element of a Story","Setting","Theme","Character","Plot",3, "What's in a Story"));
        quiz.add(new Quiz("Which of these is not part of the setting","Place","Characters","Time","Mood",1, "What's in a Story"));
        quiz.add(new Quiz("Which of these do authors use to tell you about a character","What they look like","What they are doing","How they feel","All of the above",3, "Sketch a Character"));
        quiz.add(new Quiz("What is a character’s backstory?","What a character is doing","What happened to the character before the story","The character’s feelings","The character's goals",1, "Sketch a Character"));
        quiz.add(new Quiz("What does a copy editor do?","Checks for story structure","Checks a story’s quality","Checks spelling and grammar errors","Checks that a story makes sense",0, "The Shape of Words to Come"));
        quiz.add(new Quiz("The goal of critique is","to express an opinion","to make a piece better","to pan a work","to make the writer feel good",1, "The Shape of Words to Come"));
        quiz.add(new Quiz("T.S. Elliot and Ezra Pound were best known for writing","Free Verse Poetry","novels","Sonnets","Dramatic Poetry",0, "Be a Free Verse Poet"));
        quiz.add(new Quiz("The rhythm of a poem is determined by","The number of syllables","The length of words","The stressed and unstressed syllables","The number of lines",2, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Sound Bonding is","Using a word more than once","Using words that sound similar","writing long stanzas","Using onomatopoeia" ,1, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Dramatic Poetry is","Poetry with a sad mood","Poetry that rhymes","Recited Poetry that tells a story","Poetry set to music",2, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Which of these is a simile?","She is as pale as winter","She is winter","She likes winter","She lives in winter",0, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Which of these is a metaphor?","She is as pale as winter","She is winter","She likes winter","She lives in winter",1, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Imagery is used to","Tell a reader how to feel","Create a visual in the reader’s mind","Convey a theme","Describe an action",1, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Tone is used to","Create a visual in the reader’s mind","Convey a theme","Describe an action","Tell a reader how to feel",3, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Mood is","the atmosphere of a piece","the meaning of a piece","the words used in a piece","the characters in a piece",0, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Childish and Youthful mean the same thing, but have different feelings. This is","Connotation","Denotation","Simile","Metaphor",0, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Connotation is","The literal meaning of a word","The sound of a word","What a word implies","The syllables in a word",2, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Denotation is","What a word implies","The literal meaning of a word","The sound of a word","The syllables in a word",1, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Which of these is not a kind of metaphor","Direct","Implied","Extended","Retracted",3, "Be a Free Verse Poet"));
        quiz.add(new Quiz("Reading a work and giving helpful feedback is called", "panning", "support", "critique", "review", 2, "Be a Free Verse Poet"));
        quizCategories.put("Writer", (ArrayList) quiz.clone());
        categoryMap.put("Writer", new Category("Writer", quiz));

        quiz.clear();

        quiz.add(new Quiz("Amoebas are…", "single cell organisms", "bacteria", "multi cell organisms", "viruses", 0, "Amoebas"));
        quiz.add(new Quiz("What does an amoeba use to move?","nucleus","vacuole","pseudopods","cytoplasm",2, "Amoebas"));
        quiz.add(new Quiz("What does an amoeba use pseudopods for","digesting","osmosis","moving and eating","thinking",2, "Amoebas"));
        quiz.add(new Quiz("Which is not true about Amoebas?","They have multiple cells","They can move","They don’t eat","They are alive",0, "Amoebas"));
        quiz.add(new Quiz("How does an amoeba alter its shape?","extending and retracting its membrane","growing and shrinking","subdividing itself","crystalizing",0, "Amoebas"));
        quiz.add(new Quiz("What is an amoeba’s pseudopod?","control center of a cell","bubble of digested food","projections of an amoeba’s membrane ","watery material in the amoeba’s cell ",2, "Amoebas"));
        quiz.add(new Quiz("Which taxonomic group do amoebae not belong to","protazoa","algae","fungi","bacteria",3, "Amoebas"));
        quiz.add(new Quiz("what process do amoebae use to ingest their food?","phagocytosis","osmosis","photosynthesis","homeostasis",0, "Amoebas"));
        quiz.add(new Quiz("What part of the human body are amoebae?","white blood cells","red blood cells","synapses","gut flora",0, "Amoebas"));
        quiz.add(new Quiz("Which of these do amoebae not eat","bacteria","meat","dead organisms","algae",1, "Amoebas"));
        quiz.add(new Quiz("What part of the brain controls emotion and impulse?","Parietal Lobe","Temporal Lobe","Brain Stem","Frontal Lobe",3, "Your Amazing Brain"));
        quiz.add(new Quiz("What does the Parietal Lobe control?","Movement","Memory","Senses","Breathing",2, "Your Amazing Brain"));
        quiz.add(new Quiz("The brain is divided into different sections called","Zones","Nodes","Lobes","Regions",2, "Your Amazing Brain"));
        quiz.add(new Quiz("Injuring your Temporal lobe can cause problems with","Memory","Senses","Movement","Breathing",0, "Your Amazing Brain"));
        quiz.add(new Quiz("The brain stem controls","Senses","Movement","Memory","Breathing",3, "Your Amazing Brain"));
        quiz.add(new Quiz("The motor cortex is involved with","Senses","Movement","Memory","Breathing",1, "Your Amazing Brain"));
        quiz.add(new Quiz("What kind of game works your Frontal Lobe?","Logic Puzzles","First Person Shooters","Music Games","Memory Games",0, "Your Amazing Brain"));
        quiz.add(new Quiz("What system of the body allows an animal to move","Muscular","Skeletal","Endocrine","Respiratory",0, "Muscle Mania"));
        quiz.add(new Quiz("Which of these makes up muscles?","Fats","Protein","Bone","Acids",1, "Muscle Mania"));
        quiz.add(new Quiz("What part of the body is a muscle?","brain","femur","heart","ear",2, "Muscle Mania"));
        quiz.add(new Quiz("How does a muscle move?","retraction","osmosis","contraction","spasms",2, "Muscle Mania"));
        quiz.add(new Quiz("Muscle movement is","voluntary","involuntary","voluntary and involuntary","neither",2, "Muscle Mania"));
        quiz.add(new Quiz("What anchors a muscle to a bone","tendons","fat","ligaments","nerves",0, "Muscle Mania"));
        quiz.add(new Quiz("The muscle in your legs are","Smooth muscles","Cardiac Muscles","Skeletal muscles","Large muscles",2, "Muscle Mania"));
        quiz.add(new Quiz("Food is moved through the digestive system by","involuntary muscle movement","nerve twitches","gravity","acid",0, "Muscle Mania"));
        quiz.add(new Quiz("Cardiac Muscle is found in the","heart","brain","femur","ear",0, "Muscle Mania"));
        quiz.add(new Quiz("What causes muscles to contract?","brain waves","air","lactic acid","electrical pulses",3, "Muscle Mania"));
        quiz.add(new Quiz("The strongest human muscle is the","quad","bicep","jaw","eye",2, "Muscle Mania"));
        quizCategories.put("Doctor", (ArrayList) quiz.clone());
        categoryMap.put("Doctor", new Category("Doctor", quiz));

        quiz.clear();

        quiz.add(new Quiz("When doing a redesign of a game or toy, what is the first step?", "Playing the original game", "Brainstorming ideas", "Coming up with a name", "Drawing a picture", 0, "amoebas"));
        quiz.add(new Quiz("Which of these is not a benefit of outdoor play?", "Increases fitness level", "Improves scores in reading, writing, and listening", "Reduces stress", "Improved Near Vision", 3, "amoebas"));
        quiz.add(new Quiz("Playing together can help adults in","Conflict Management","Creativity","Reducing Stress","All of the Above",3,"amoebas"));
        quiz.add(new Quiz("Which activity is not Play","Hide and Seek","laughing with friends","Addictive Gambling","Doing math puzzles for fun",2,"amoebas"));
        quiz.add(new Quiz("Which is not an element of game design?","Space","Code","Goals","Mechanic",1,"amoebas"));
        quiz.add(new Quiz("In Game Design, Space is:","The look and feel of the game","The size of the screen","The setting of the game","The length of a game",0,"amoebas"));
        quiz.add(new Quiz("In Game Design, Goals are:","The final level","When the game is built","How the player wins","What the player feels",2,"amoebas"));
        quiz.add(new Quiz("In Game Design, Components are:","The code of the game","What the player can and cannot do","The objects and characters in the game world","Actions the player can do",2,"amoebas"));
        quiz.add(new Quiz("In Game Design, Mechanics are:","The code of the game","Actions the player can do","The objects and characters in the game world","What the player can and cannot do",1,"amoebas"));
        quiz.add(new Quiz("In Game Design, Rules are:","The code of the game","Actions the player can do","The objects and characters in the game world","What the player can and cannot do",3,"amoebas"));
        quiz.add(new Quiz("What is the first Video Game?","Pong","SpaceWar!","Donkey Kong","Space Invaders",1,"amoebas"));
        quiz.add(new Quiz("Which of these is a real life example of an achievement mechanic?","Employee-of-the-Month award","Winning the Lottery","Climbing a Mountain","Playing X-Box",0, "amoebas"));
        quiz.add(new Quiz("Losing yourself in a game is known as", "Passion", "Flow", "Interest Curve","Hypnosis",1, "amoebas"));
        quiz.add(new Quiz("Brainstorming is", "Making To-Do Lists", "convincing others to do your idea", "Coming up with ideas with no restraints", "Settling on an idea",2, "amoebas"));
        quiz.add(new Quiz("A Prototype is", "The final game", "A broken version of the game", "A quick version of what you want to make", "The first level of the game",2,"amoebas"));
        quiz.add(new Quiz("Good Directions","Explains everything in the game","Can be understood by someone who has never played the game","Are easy to read","All of the Above",3,"amoebas"));
        quiz.add(new Quiz("A Playtest is:","Letting players try your game design ","Showing off your game to people","Giving people your game","Releasng your game",0,"amoebas"));
        quiz.add(new Quiz("During a playtest you should","Tell players what to do","Take notes on what players do","Play the game by yourself","Change the game while people play",1,"amoebas"));
        quiz.add(new Quiz("Iterating is","Making many different games","Polishing the game art","Starting over on the game","Making changes to a game based on playtests",3,"amoebas"));
        quiz.add(new Quiz("When should you have a playtest?","After iterating on the game","When the game is done","Every month","when you feel like it",0, "amoebas"));
        quiz.add(new Quiz("How many times should you iterate?", "twice", "once", "Until the playtests show no problems", "Until the rules of the game are clear",2,"amoebas"));
        quizCategories.put("Game Designer", (ArrayList) quiz.clone());
        categoryMap.put("Game Designer", new Category("Game Designer", quiz));

        quiz.clear();

        quiz.add(new Quiz("What is the difference between libel and slander?", "Libel is written defamatory statement and slander is spoken", "Libel is a positive statement and slander is a defamatory statement", " Libel is spoken defamatory statement and slander is written", " Libel is a statement presumed to be defamatory and slander is proven as defamatory", 0,"Alert the Media"));
        quiz.add(new Quiz("How could a journalist commit defamation?","Publishing inaccurate information about a person or business","Publishing popular public opinion about a person or business"," Publishing their own opinions about a person or business"," All of the above",3,"Alert the Media"));
        quiz.add(new Quiz("Which of the following is not a type of a media bias?","Bias by omission","Bias by placement"," Bias by addition"," Bias by labeling",2,"Alert the Media"));
        quiz.add(new Quiz("Which of the following can negatively affect the credibility of news coverage?","Lack of diversity ","Source Selection"," Elevated Language "," All of the Above",3,"Alert the Media"));
        quiz.add(new Quiz("Which of the following would be the least reliable news website?","Citizen journalism forum","Social Media"," A local newspaper’s website"," Satirical website ",3,"Alert the Media"));
        quiz.add(new Quiz("What is a lead?","The summary paragraph of an article ","The main idea of an article"," The opening paragraph of an article"," The angle of the article",2,"Art of the Article"));
        quiz.add(new Quiz("What comes first in the inverted pyramid structure of a news story?","Quotes from the main source","All of the most important facts on the topic"," An anecdotal scenario to set the scene"," The writer’s opinion on the topic",1,"Art of the Article"));
        quiz.add(new Quiz("What qualities do you look for in a potential source?","Popularity ","Availability "," Expertise"," All of the above",2,"Art of the Article"));
        quiz.add(new Quiz("Which of the following is an example of paraphrasing?","\"The weather is getting cold,\" Alex says","Alex remarked on the quick change of the weather as temperatures droppe "," On Thursday, I met with Alex to discuss his views on the recent weather. "," Alex believes that global warming is responsible for the change of weather. ",1,"Art of the Article"));
        quiz.add(new Quiz("When should you use a direct quote in a news article?","Whenever possible","When the quote voices an opinion that supports your point of view"," When the quote adds evidence or new information to the article "," When the article is too heavy on research and summary",2,"Art of the Article"));
        quiz.add(new Quiz("Which of the following is not an aspect of rhetoric?","Ethos","Lathos"," Pathos"," Logos",1,"Pursuade Me"));
        quiz.add(new Quiz("In advertising, which of the following persuasive techniques uses the argument that 'everybody else is doing it'?","Bandwagon","Generalities "," Bait and Switch "," Name-calling",0,"Pursuade Me"));
        quiz.add(new Quiz("Which of the following is not a purpose Public Service Announcements?","Raise awareness ","Change public opinion"," Highlight a group or organization"," Promote a product",3,"Pursuade Me"));
        quizCategories.put("Journalist", (ArrayList) quiz.clone());
        categoryMap.put("Journalist", new Category("Journalist", quiz));

        quiz.clear();

        quiz.add(new Quiz("The technology of cell phone or tablet cameras is most similar to…", "Full frame DSLR cameras", "Crop sensor DSLR cameras", "Point-and-shoot compact cameras ", "Film cameras", 2, "Mobile Masterpiece"));
        quiz.add(new Quiz("Many people regard Mobile Photography as an art form because…", "It is technologically equal to professional photography ", "Photos can be taken in unique, meaningful ways", "Many photographers are ditching their heavy equipment for mobile phones", "Museums and galleries opt to showcase a more modern medium", 1, "Mobile Masterpiece"));
        quiz.add(new Quiz("Which of the following is not an aspect of photographic composition:","The Rule of Thirds","Angel of the camera","Camera Settings ","Lighting ",2,"Keeping Composed"));
        quiz.add(new Quiz("In photography, repetition refers to…","High speed camera drive","Capturing the same subject at different times of the day","Repeating objects, shapes or patterns in a shot ","Repeating photo shoots in hopes to improve ",2,"Keeping Composed"));
        quiz.add(new Quiz("What is a doodle video?","An step-by-step drawing instruction","A fast paced video of sketches that promote a product or topic ","The prototype of an animated film or show","A demonstration of various techniques to create a simple drawing",1,"Doodle Video"));
        quiz.add(new Quiz("What is the first step in making a duct tape wallet or any accessory?","Collect Materials","Sketch the Design ","Make a prototype of the design","Use your wallet",1,"Duct Tape Wallet"));
        quiz.add(new Quiz("When was the modern wallet invented?","3300 BC","1400s","1600s","1800s",2,"Duct Tape Wallet"));
        quiz.add(new Quiz("Which is proven to be a result of crocheting?","Increased intelligence ","Longer life span ","Friendliness ","Stress relief ",3,"Crocheting"));
        quiz.add(new Quiz("Which of the following is the most common type of crochet hook?","Aluminum","Steel","Plastic","Tunisian",0,"Crocheting"));
        quiz.add(new Quiz("How are crochet patterns rated and organized?","Amount of yarn needed","Number of stitches ","Size of product","Level of difficulty",3,"Crocheting"));
        quiz.add(new Quiz("When looking at pieces of prominent art from the 1940s, you would likely see traces of…","Commentary on social classes","The political effects of WWII","Westward expansion in North America ","The first experimentations with impressionism",1,"See History through Art"));
        quizCategories.put("Artist", (ArrayList) quiz.clone());
        categoryMap.put("Artist", new Category("Artist", quiz));
    }

    private void loadChallenge(){
        ArrayList<Challenge> challenges = new ArrayList<>();
        ArrayList<String> preTexts = new ArrayList<String>();
        preTexts.add("Teachers help guide their students to the future. Part of this is having an optimistic outlook for their lives, and their students.");
        preTexts.add("Get at least one friend to play this challenge. The more the better");
        preTexts.add("Name a problem you might have in your daily life. For example, “I left my lunch at home.”");
        preTexts.add("Now, your friend will name a Sunny Side to this problem. For example “My friend showed how much they care by giving me some of theirs.”");
        preTexts.add("Now it is your friend’s turn to name a problem, and for you to name a Sunny Side. Keep Going until one of you is stuck thinking of a problem or a Sunny Side");
        Challenge challenge = new Challenge("Keep on the Sunny Side",
                (ArrayList<String>) preTexts.clone(),
                "Think of your problem!",
                "Name your Sunny Side!",
                "Great! Practicing looking on the Sunny Side will help you to be Optimistic, a skill that helps Teachers face challenges.\n");
        challenge.categoryType = AchievementSystem.CategoryType.TEACHER;
        challenges.add(challenge);
        preTexts.clear();
        personalChallenge.put("Teacher", (ArrayList<Challenge>)challenges.clone());
        challenges.clear();

        preTexts.add("The body is an amazing thing. When we get stressed our body goes through changes");
        preTexts.add("We go into ‘fight or flight mode’ our body tenses up and our breathing gets shallow");
        preTexts.add("Recognizing this and practicing breathing exercises can help to relieve stress");
        preTexts.add("For this challenge, close your eyes. Breathe in, and count to four. Breathe out while counting to eight.");
        preTexts.add("Do this for a minute. See if you can feel your body change.");
        Challenge challenge2 = new Challenge("Know your Body",
                (ArrayList<String>) preTexts.clone(),
                "Close Your Eyes and Relax",
                "Close your eyes and breathe",
                "Open your eyes! How do you feel? Practicing this any time you feel anxious will help you Handle Stress.");
        challenge2.gameType = Challenge.GameType.TIMER;
        challenge2.categoryType = AchievementSystem.CategoryType.DOCTOR;
        challenges.add(challenge2);
        preTexts.clear();
        personalChallenge.put("Doctor", (ArrayList<Challenge>) challenges.clone());
        challenges.clear();

        preTexts.add("Great stories are not always created in a vacuum. Many writers, such as film writers, work with other people to develop stories");
        preTexts.add("Get at least one friend to play this challenge! The more the better!");
        preTexts.add("The goal of the challenge is to tell a story, with everybody saying one word at a time.");
        preTexts.add("One person says a word, and then the next person says the next to continue the story. ");
        preTexts.add("Players continue to tell the story. Try to tell a story that makes sense ");
        preTexts.add("The story ends when one player says ‘The’ and the next says ‘End.’");
        Challenge challenge3 = new Challenge("One Word Story",
                (ArrayList<String>) preTexts.clone(),
                "Get your friends and get ready!",
                "Tell Your Story!",
                "Great! You may not have told a different story than you would have by yourself. By listening and collaborating with you friend, you created something truly unique!\n" +
                        "\n" +
                        "Keep playing this. This fun game will help you grow as a storyteller.");
        challenge3.categoryType = AchievementSystem.CategoryType.WRITER;
        challenges.add(challenge3);
        preTexts.clear();
        personalChallenge.put("Writer", (ArrayList<Challenge>)challenges.clone());
        challenges.clear();

        preTexts.add("Artists tap into a lot of sources for inspiration - including their own feelings");
        preTexts.add("For this challenge you will need pencil and paper. If you have other art supplies you may want to use that as well");
        preTexts.add("Stop and think about how you feel right now. Happy? Angry? Stressed? Close your eyes and picture this feeling");
        preTexts.add("Now draw your feeling. You can draw people and things, or you can be abstract and only draw shapes and colors. What matters is what your feeling looks like to you.");;
        Challenge challenge4 = new Challenge("Drawing Feelings",
                (ArrayList<String>) preTexts.clone(),
                "Get your drawing equipment!",
                "Think on your feelings. When you are ready, start drawing.",
                "Great. Thinking about what your feelings are helps you Manage Emotion. Drawing can be one great release.\n" +
                        "\n" +
                        "You can share your picture or keep it to yourself. Keep practicing recognizing and expressing your emotions.");
        challenge4.categoryType = AchievementSystem.CategoryType.ARTIST;
        challenges.add(challenge4);
        preTexts.clear();
        personalChallenge.put("Artist", (ArrayList<Challenge>)challenges.clone());
        challenges.clear();

        preTexts.add("Game Designers come up with new game ideas every day. Often the best ideas are created when two or more people work together.");
        preTexts.add("Get at least one friend to play this challenge. You may also want a paper and pencil to write or draw ideas.");
        preTexts.add("You and your friend take turns coming up with suggestions for your game. For example, you come up with the name, and your friend comes up with the main character. Then you may come up with how the game is played.");
        preTexts.add("Every suggestion is added in - you cannot reject a suggestion. As you name things, you may write or draw your game ideas.");
        Challenge challenge5 = new Challenge("Make a Game",
                (ArrayList<String>) preTexts.clone(),
                "Get your equipment and your friend!",
                "Start designing your game",
                "Awesome! You probably came up with a very unique game idea. Collaborating lets you create something you wouldn’t create on your own! \n" +
                        "\n" +
                        "How did it feel to work with someone else’s ideas? Practice this skill to get even better at making good designs.");
        challenge5.categoryType = AchievementSystem.CategoryType.GAMER;
        challenges.add(challenge5);
        preTexts.clear();
        personalChallenge.put("Game Designer", (ArrayList<Challenge>)challenges.clone());
        challenges.clear();

        preTexts.add("Every day, Journalists have to interview people.");
        preTexts.add("Sometimes talking to strangers can make you nervous. Practicing this helps.");
        preTexts.add("For this challenge, go to someone you do not know very well, and introduce yourself.");
        preTexts.add("Ask the person their name, and one more question, such as what is the time.");
        Challenge challenge6 = new Challenge("Small Talk",
                (ArrayList<String>) preTexts.clone(),
                "Think of your questions!",
                "Find someone and ask them their name, and one more question.",
                "Great! Practicing this will help your leadership skills, something every teacher has.");
        challenge6.setChallengeCategory(AchievementSystem.CategoryType.JOURNALIST);
        challenges.add(challenge6);
        preTexts.clear();
        personalChallenge.put("Journalist", (ArrayList<Challenge>) challenges.clone());
        challenges.clear();

    }

    public void startPartyGame(ArrayList<String> names)
    {
        //Only call partyGame when initializing, after this call partyGame directly
        //I know it's wrong, it is fast, we don't have time
        partyGame = new PartyGame();
        partyGame.startGame(names);
    }
}
