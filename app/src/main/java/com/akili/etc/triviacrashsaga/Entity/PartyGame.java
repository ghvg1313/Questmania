package com.akili.etc.triviacrashsaga.Entity;

import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.CenterController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by kangleif on 10/30/2015.
 */
public class PartyGame {

    public static ArrayList<Player> players;
    public static int moderatorIndex = 0;
    public static int currentComponentIndex = 0;
    public static String categoryName;
    public int roundIndex = 1;
    public static boolean roundCategorySelected = false;

    public static ArrayList<Challenge> challenges;
    public static ArrayList<Integer> challengeIndices;

    public void startGame(ArrayList<String> names){
        players = new ArrayList<>();

        for(String name : names){
            Player player = new Player(name);
            players.add(player);
        }

        moderatorIndex = 0;
        currentComponentIndex = 1;
        roundIndex = 1;

        loadChallenge();

        challengeIndices = new ArrayList<>();
        for(int i=0;i<challenges.size();i++){
            challengeIndices.add(i);
        }
    }

    public static Player getModerator() {
        return players.get(moderatorIndex);
    }

    public static Player getComponent(){
        return players.get(currentComponentIndex);
    }

    public boolean nextRound() {
        if(roundIndex + 1 > players.size()){
            return false;
        } else{
            moderatorIndex++;
            currentComponentIndex = 0;
            roundIndex++;
            roundCategorySelected = false;
            return true;
        }
    }

    public Challenge getNextChallenge(){
        Random randomizer = new Random();
        if(challengeIndices.size() == 0){
            for(int i=0;i<challenges.size();i++){
                challengeIndices.add(i);
            }
        }
        int resultIndex = randomizer.nextInt(challengeIndices.size());
        int result = challengeIndices.get(resultIndex);
        challengeIndices.remove(resultIndex);
        return challenges.get(result);
    }

    public ArrayList<Player> getWinners()
    {
        int highScore = 0;
        for(Player player : players){
            if(player.partyModeScore >= highScore){
                highScore = player.partyModeScore;
            }
        }

        ArrayList<Player> winners = new ArrayList<>();

        for(Player player : players){
            if(player.partyModeScore == highScore){
                winners.add(player);
            }
        }
        return players;
    }

    private void loadChallenge(){
        challenges = new ArrayList<>();
        Challenge challenge;

        ArrayList<String> preTexts = new ArrayList<String>();
        preTexts.add("Challengers are running for President of the country of Chamelia, and it is time for the debate!");
        preTexts.add("<moderator>" +" will read the topic. Each player will make up an opinion on that topic in turn.");
        preTexts.add("The players will go around again, saying why their opinion is correct, and the others are wrong.");
        preTexts.add("<moderator>" +" then picks the player that made the best argument.");

        challenge = new Challenge(Challenge.PreGameType.VIDEO_TEXT,
                Challenge.PreTimerType.TEXT_TIMER,
                Challenge.GameType.PLAYER_TURN,
                "Own Your Choice",
                preTexts,
                "What Color should the flag of Camellia be?",
                "Prepare your remarks",
                "Who gave the best answer?");
        HashMap<String, String> mask = new HashMap<>();
        mask.put("Writer", "Writers have to Own their Choice, even when it’s controversial.");
        mask.put("Doctor", "Doctors have to make choices in order to help patients. They must be able to Own their Choice.");
        mask.put("Teacher", "Teachers have to make choices in how to best teach their students. They must Own their Choice");
        mask.put("Game Designer", "Game Designers have to make design decisions. They must Own their Choice");
        mask.put("Journalist", "Journalists must make choices in what they investigate and present to the public. They must Own their Choice");
        mask.put("Artist", "Artists must make choices in what they create. They must Own their Choice");

        challenge.imageResourceId = R.drawable.pic_challenge_1;

        challenge.mask = (HashMap<String, String>)mask.clone();

        challenge.audioTexts.add(R.raw.challenge_the_debate_1);
        challenge.audioTexts.add(R.raw.challenge_the_debate_2);
        challenge.audioTexts.add(R.raw.challenge_the_debate_3);
        challenge.audioTexts.add(R.raw.challenge_the_debate_4);
//        challenge.maskAudio.put("Writer", R.raw.challenge_the_debate_writer);
//        challenge.maskAudio.put("Doctor", R.raw.challenge_the_debate_doctor);
//        challenge.maskAudio.put("Teacher", R.raw.challenge_the_debate_teacher);
//        challenge.maskAudio.put("Game Designer", R.raw.challenge_the_debate_gamer);
//        challenge.maskAudio.put("Journalist", R.raw.challenge_the_debate_journalist);
//        challenge.maskAudio.put("Artist", R.raw.challenge_the_debate_artist);


        Challenge challenge2;
        ArrayList<String> preTexts2 = new ArrayList<String>();
        preTexts2.add("Who among the challengers is the best listener?");
        preTexts2.add("<moderator>" +" will clap.\n If "+"<moderator>"+" claps once, challengers stand on one foot.\nTwo claps, the challenger jump up and down\nThree claps, the challengers stand on both feet and rub their bellies.");
        preTexts2.add("<moderator>" +" keeps clapping. If a challenger does the wrong move or pauses, they are out. The last challenger standing wins!");
        preTexts2.add("Remember challengers!\n" +
                "1 Clap = Stand on one foot\n" +
                "2 Claps = Jump up and down\n" +
                "3 Claps = Stand on both feet and rub your belly");

        challenge2 = new Challenge(Challenge.PreGameType.VIDEO_TEXT,
                Challenge.PreTimerType.TEXT_TIMER,
                Challenge.GameType.TEXT_IMAGE_DISQUALIFY,
                "Stand Hop Rub",
                preTexts2,
                "",
                "Stand up and get ready!",
                "Remember challengers!\n1 Clap = Stand on one foot\n2 Claps = jump up and down\n3 Claps = stand on both feet and rub your belly");
        mask = new HashMap<>();
        mask.put("Writer", "Good Writers learn to Listen to others so they know how to communicate ideas to them.");
        mask.put("Doctor", "Doctors have to Listen carefully to their patients in order to figure out what is wrong with them.");
        mask.put("Teacher", "Good Teachers Listen to their students to understand how they are learning.");
        mask.put("Game Designer", "Game Designers must Listen to their audience, colleagues, and Producers.");
        mask.put("Journalist", "Journalists must listen to people when they interview them.");
        mask.put("Artist", "Good Artists Listen to people and incorporate what they hear into their art.");

        challenge2.imageResourceId = R.drawable.pic_challenge_2;

        challenge2.mask = (HashMap<String, String>)mask.clone();

        challenge2.audioTexts.add(R.raw.challenge_hop_stand_rub_1);
        challenge2.audioTexts.add(R.raw.challenge_hop_stand_rub_2);
        challenge2.audioTexts.add(R.raw.challenge_hop_stand_rub_3);
        challenge2.audioTexts.add(R.raw.challenge_hop_stand_rub_4);
//        challenge2.maskAudio.put("Writer", R.raw.challenge_hop_stand_rub_writer);
//        challenge2.maskAudio.put("Doctor", R.raw.challenge_hop_stand_rub_doctor);
//        challenge2.maskAudio.put("Teacher", R.raw.challenge_hop_stand_rub_teacher);


        ArrayList<String> preTexts3 = new ArrayList<String>();
        preTexts3.add("<moderator>" + " will say two truths, and one lie about themselves.");
        preTexts3.add("Challengers will take turns saying which statement they believe is a lie and why. Challengers cannot ask " + "<moderator>" + " questions.");
        preTexts3.add("At the end, " + "<moderator>" + " picks the player with the best answer.");
        Challenge challenge3 = new Challenge(Challenge.PreGameType.VIDEO_TEXT,
                Challenge.PreTimerType.TEXT_TIMER,
                Challenge.GameType.PLAYER_TURN,
                "Truth and Lie",
                preTexts3,
                "Which statement is the lie, and why?",
                "<moderator>" +", prepare your statements",
                "Who gave the best answer?");
        challenge3.moderatoTurnText = "Give two truths and a lie now.";
        mask = new HashMap<>();
        mask.put("Writer", "Writers have to understand other people’s points of view to write good stories. They have to be able to Relate.");
        mask.put("Doctor", "Doctors have to understand their patient’s points of view to help them. They have to be able to Relate.");
        mask.put("Teacher", "Teachers have to understand their student’s points of view to teach them well. They have to be able to Relate.");
        mask.put("Game Designer", "Game Designers have to understand how people will play their games. They must be able to Relate");
        mask.put("Journalist", "Journalists need to Relate to others in order to tell their stories.");
        mask.put("Artist", "Good Artists Relate to others so that they can create art that will speak to them.");

        challenge3.imageResourceId = R.drawable.pic_challenge_3;

        challenge3.mask = (HashMap<String, String>)mask.clone();

        challenge3.audioTexts.add(R.raw.challenge_truth_and_lie_1);
        challenge3.audioTexts.add(R.raw.challenge_truth_and_lie_2);
        challenge3.audioTexts.add(R.raw.challenge_truth_and_lie_3);
//        challenge3.maskAudio.put("Writer", R.raw.challenge_truth_and_lie_writer);
//        challenge3.maskAudio.put("Doctor", R.raw.challenge_truth_and_lie_doctor);
//        challenge3.maskAudio.put("Teacher", R.raw.challenge_truth_and_lie_teacher);
//        challenge3.maskAudio.put("Game Designer", R.raw.challenge_truth_and_lie_gamer);
//        challenge3.maskAudio.put("Journalist", R.raw.challenge_truth_and_lie_journalist);
//        challenge3.maskAudio.put("Artist", R.raw.challenge_truth_and_lie_artist);


        ArrayList<String> preTexts4 = new ArrayList<String>();
        preTexts4.add("<moderator> thinks of a setting, a character, and a something that happens to the character.");
        preTexts4.add("<challengers> each think of a story. Then each challenger tells their story.");
        preTexts4.add("The story should be from the point of view of the character, and describe how the character feels.");
        preTexts4.add("The best story wins.");
        Challenge challenge4 = new Challenge(Challenge.PreGameType.VIDEO_TEXT,
                Challenge.PreTimerType.TEXT_TIMER,
                Challenge.GameType.PLAYER_TURN,
                "My Story",
                preTexts4,
                "<moderator>, think of a setting, character, and event",
                "Tell your story! Remember, the story is first person, and describes how you feel.",
                "Who told the best story?");
        challenge4.moderatoTurnText = "Give your setting, character and event now.";
        mask = new HashMap<>();
        mask.put("Writer", "Writers have to understand other people’s points of view to write good stories. They have to be able to Relate.");
        mask.put("Doctor", "Doctors have to understand their patient’s points of view to help them. They have to be able to Relate.");
        mask.put("Teacher", "Teachers have to understand their student’s points of view to teach them well. They have to be able to Relate.");
        mask.put("Game Designer", "Game Designers have to understand how people will play their games. They must be able to Relate");
        mask.put("Journalist", "Journalists need to Relate to others in order to tell their stories.");
        mask.put("Artist", "Good Artists Relate to others so that they can create art that will speak to them.");

        challenge4.imageResourceId = R.drawable.pic_challenge_4;

        challenge4.mask = (HashMap<String, String>)mask.clone();

        challenge4.audioTexts.add(R.raw.challenge_my_story1);
        challenge4.audioTexts.add(R.raw.challenge_my_story2);
        challenge4.audioTexts.add(R.raw.challenge_my_story3);
        challenge4.audioTexts.add(R.raw.challenge_my_story4);

        Challenge challenge5;
        ArrayList<String> preTexts5 = new ArrayList<String>();
        preTexts5.add("<challengers> must stand on one leg and hold their arms in the air.");
        preTexts5.add("Players can switch legs, but must always keep one in the air. ");
        preTexts5.add("<moderator> watches and disqualifies anyone that  puts both feet on the ground or puts their arms down. ");
        preTexts5.add("The last player standing is the winner.");

        challenge5 = new Challenge(Challenge.PreGameType.VIDEO_TEXT,
                Challenge.PreTimerType.TEXT_TIMER,
                Challenge.GameType.TEXT_IMAGE_DISQUALIFY,
                "Hold That Pose",
                preTexts5,
                "",
                "Stand up and get ready!",
                "The last player standing is the winner!");
        mask = new HashMap<>();
        mask.put("Writer", "Writers have to Persevere, even in the face of criticism.");
        mask.put("Doctor", "Doctors have to Persevere even under stress.");
        mask.put("Teacher", "Teachers have to Persevere in making sure their students get the knowledge they need.");
        mask.put("Game Designer", "Game Designers have to Persevere to get the best design");
        mask.put("Journalist", "Journalists have to Persevere to find the truth.");
        mask.put("Artist", "Artists have to Persevere to improve their art skills");

        challenge5.imageResourceId = R.drawable.pic_challenge_5;

        challenge5.mask = (HashMap<String, String>)mask.clone();

        challenge5.audioTexts.add(R.raw.challenge_hold_that_pose1);
        challenge5.audioTexts.add(R.raw.challenge_hold_that_pose2);
        challenge5.audioTexts.add(R.raw.challenge_hold_that_pose3);
        challenge5.audioTexts.add(R.raw.challenge_hold_that_pose4);


        challenges.add(challenge);
        challenges.add(challenge2);
        challenges.add(challenge3);
        challenges.add(challenge4);
        challenges.add(challenge5);

    }
}
