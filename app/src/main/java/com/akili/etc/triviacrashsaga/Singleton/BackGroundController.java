package com.akili.etc.triviacrashsaga.Singleton;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import com.akili.etc.triviacrashsaga.R;

import java.io.IOException;

public class BackGroundController {

    public enum playingBGM{
        NONE,
        NORMAL,
        CHALLENGE,
        BADGE,
        WIN,
        TRIVIA,
        FINAL
    }

    public static MediaPlayer mp;
    public static MediaPlayer audioPlayer;
    public static MediaPlayer soundEffectPlayer;

    public static playingBGM nowPlaying = playingBGM.NONE;

    private static void playMP3(Context c, int song, boolean looping){
        if(mp != null){
            mp.release();
        }
        mp = MediaPlayer.create(c, song);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(looping);
        mp.start();
    }

    private static void playSound(Context c, int song, boolean stopPrevious){
        if(stopPrevious && audioPlayer != null){
            audioPlayer.release();
        }
        audioPlayer = MediaPlayer.create(c, song);
        audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if(mp!=null) {
            mp.setVolume(0.2f, 0.2f);
        }
        audioPlayer.start();
    }

    private static void playSoundEffect(Context c, int song, boolean stopPrevious){
        if(stopPrevious && soundEffectPlayer != null){
            soundEffectPlayer.release();
        }
        soundEffectPlayer = MediaPlayer.create(c, song);
        soundEffectPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        soundEffectPlayer.start();
    }

    public static void playNormalBGM(Context c){
        if(nowPlaying != playingBGM.NORMAL) {
            nowPlaying = playingBGM.NORMAL;
            playMP3(c, R.raw.challenge_menu_bgm, true);
        }
    }

    public static void playChallengeBGM(Context c){
        if(nowPlaying != playingBGM.CHALLENGE) {
            nowPlaying = playingBGM.CHALLENGE;
            playMP3(c, R.raw.challenge_game_bgm, true);
        }
    }

    public static void playTriviaBGM(Context c){
        if(nowPlaying != playingBGM.TRIVIA) {
            nowPlaying = playingBGM.TRIVIA;
            playMP3(c, R.raw.challenge_trivia_bgm, true);
        }
    }

    public static void playBadgeBGM(Context c){
        nowPlaying = playingBGM.BADGE;
        playMP3(c, R.raw.badge, false);
    }


    public static void playWinBGM(Context c){
        nowPlaying = playingBGM.WIN;
        playMP3(c, R.raw.challenge_win_bgm, false);
    }

    public static void playFinalWinBGM(Context c){
        nowPlaying = playingBGM.FINAL;
        playSoundEffect(c, R.raw.final_winner);
    }

    public static void playSoundEffect(Context c, int song){
        playSoundEffect(c, song, true);
    }

    public static void playAudio(Context c, int song){
        playSound(c, song, true);
    }

    public static void stop(){
        nowPlaying = playingBGM.NONE;
        if(mp!=null) {
            mp.release();
        }
    }

    public static void pause(){
        mp.pause();
    }

    public static void resume(){
        mp.start();
    }

}
