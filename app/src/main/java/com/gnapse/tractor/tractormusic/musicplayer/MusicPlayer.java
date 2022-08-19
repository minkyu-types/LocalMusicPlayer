package com.gnapse.tractor.tractormusic.musicplayer;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;
import com.gnapse.tractor.tractormusic.model.MusicItem;
import com.gnapse.tractor.tractormusic.model.MusicRepository;
import java.io.IOException;
import java.util.List;

public class MusicPlayer implements BaseMusicPlayer, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    public static final String TAG = MusicPlayer.class.getSimpleName();

    private Application mApplication;
    private int mCurrPlayIndex;
    private int mPrevPlayIndex = -1;
    private boolean mIsPaused;
    private boolean mIsShuffled = false;
    private MediaPlayer mMediaPlayer;

    private List<MusicItem> mMusicItemList;
    private static MusicPlayer sInstance;

    private MusicPlayer(Application application) {
        this.mApplication = application;
        this.mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);

        setMusicItemList(MusicRepository.getInstance(mApplication).getMusicItemList());
    }

    public static MusicPlayer getInstance(Application application) {
        if (sInstance == null) {
            synchronized (MusicPlayer.class) {
                if (sInstance == null) {
                    sInstance = new MusicPlayer(application);
                }
            }
        }
        return sInstance;
    }

    public void initMediaSetting() {
        if (mMusicItemList == null) {
            mMusicItemList = MusicRepository.getInstance(mApplication).getMusicItemList();
        }

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e(TAG, "Music Thread is running. . .");
        mMediaPlayer.start();
    }

    @Override
    public void play() {
        Log.e(TAG, "PLAY");

        if (mIsPaused || mPrevPlayIndex == mCurrPlayIndex) {
            onPrepared(mMediaPlayer);
            mIsPaused = false;
        } else {
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mApplication, mMusicItemList.get(getCurrentPlayIndex()).getMusicUri());
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pause() {
        Log.e(TAG, "PAUSE");
        mMediaPlayer.pause();
        mIsPaused = true;
    }

    @Override
    public void prev() {
        mMediaPlayer.reset();

        int curr = getCurrentPlayIndex();

        if (curr >= 1) {
            curr -= 1;
            Log.e(TAG, "CURR : " + curr);
            setCurrentPlayIndex(curr);
        } else {
            Log.d(TAG, "First Item");
        }

        try {
            mMediaPlayer.setDataSource(mApplication, mMusicItemList.get(getCurrentPlayIndex()).getMusicUri());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mIsPaused = false;
    }

    @Override
    public void next() {
        mMediaPlayer.reset();

        int curr = getCurrentPlayIndex();

        if (curr <= mMusicItemList.size()) {
            curr += 1;
            Log.e(TAG, "CURR : " + curr);
            setCurrentPlayIndex(curr);
        } else {
            Log.d(TAG, "Last Item");
        }

        MusicRepository.getInstance(mApplication).setMusicTitle(MusicRepository.getInstance(mApplication).getMusicItemList().get(getCurrentPlayIndex()).getMusicTitle());

        try {
            mMediaPlayer.setDataSource(mApplication, mMusicItemList.get(getCurrentPlayIndex()).getMusicUri());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mIsPaused = false;
    }

    @Override
    public void shuffle() {
        Log.e(TAG, "Shuffled");
        mMusicItemList = MusicRepository.getInstance(mApplication).getShuffledList();
        mIsShuffled = true;
    }

    @Override
    public void repeatNone() {
        Log.e(TAG, "Repeat : false");
        mMediaPlayer.setLooping(false);
    }

    @Override
    public void repeatAll() {
        Log.e(TAG, "Repeat : list");
        int currIndex = getCurrentPlayIndex();
        int endIndex = MusicRepository.getInstance(mApplication).getMusicItemList().size();

        if (currIndex == endIndex) {
            currIndex = 0;
        }
    }

    @Override
    public void repeatThis() {
        Log.e(TAG, "Repeat : curr");
        mMediaPlayer.setLooping(true);
    }

    @Override
    public void playSelectedSong(int index) {

    }

    @Override
    public void seekTo(int sec) {
        // TO-DO
        // when user moves Seekbar thumb
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void destroyMediaPlayer() {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void setPrevPlayIndex(int prev) {
        mPrevPlayIndex = prev;
    }

    public int getPrevPlayIndex() {
        return mPrevPlayIndex;
    }

    @Override
    public int getCurrentPlayIndex() {
        return mCurrPlayIndex;
    }

    public void setCurrentPlayIndex(int position) {
        this.mCurrPlayIndex = position;
    }

    private void setMusicItemList(List<MusicItem> list) {
        mMusicItemList = list;
    }

    public void setIsShuffled(boolean shuffle) {
        mIsShuffled = shuffle;
    }

    public boolean isShuffled() {
        return mIsShuffled;
    }
}
