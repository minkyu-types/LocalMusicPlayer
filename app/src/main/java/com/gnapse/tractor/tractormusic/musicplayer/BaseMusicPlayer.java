package com.gnapse.tractor.tractormusic.musicplayer;

import android.media.MediaPlayer;

public interface BaseMusicPlayer {
    void play();
    void pause();
    void prev();
    void next();
    void shuffle();

    void repeatNone();
    void repeatAll();
    void repeatThis();

    void playSelectedSong(int index);
    void seekTo(int sec);
    int getCurrentPlayIndex();

    MediaPlayer getMediaPlayer();
}
