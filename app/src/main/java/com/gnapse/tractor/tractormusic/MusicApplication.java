package com.gnapse.tractor.tractormusic;

import android.app.Application;
import android.util.Log;

public class MusicApplication extends Application {
    public static final String TAG = MusicApplication.class.getSimpleName();

    public void isOSUpdate() {
        Log.d(TAG, "OSUpdate : True");
    }

    public void isIncomingCall() {
        // AudioManager로 이관
        Log.d(TAG, "Incoming Call : True");
    }

    public void isMute() {
        // AudioManager로 이관
        Log.e(TAG, "Mute : True");
    }
}
