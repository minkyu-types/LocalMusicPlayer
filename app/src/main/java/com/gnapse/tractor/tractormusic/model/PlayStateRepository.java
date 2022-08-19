package com.gnapse.tractor.tractormusic.model;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

public class PlayStateRepository {
    public static final String TAG = PlayStateRepository.class.getSimpleName();

    private Application mApplication;
    private static PlayStateRepository sInstance;

    public MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsShuffle = new MutableLiveData<>();
    public boolean isMute;

    private PlayStateRepository(Application application) {
        this.mApplication = application;
    }

    public static PlayStateRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (MusicRepository.class) {
                if (sInstance == null) {
                    sInstance = new PlayStateRepository(application);
                }
            }
        }
        return sInstance;
    }

    public void setPlay(boolean value) {
        Log.e(TAG, "setPlaying : " + value);
        mIsPlaying.postValue(value);
    }

    public MutableLiveData<Boolean> isPlaying() {
        return mIsPlaying;
    }

    public void setShuffle(boolean value) {
        Log.e(TAG, "setShuffle : " + value);
        mIsShuffle.postValue(value);
    }

    public MutableLiveData<Boolean> isShuffle() {
        return mIsShuffle;
    }

    public void getIsMute() {

    }

    public void setMute() {

    }

    public void setUnMute() {

    }
}
