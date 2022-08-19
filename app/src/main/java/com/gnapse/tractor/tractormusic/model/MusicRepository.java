package com.gnapse.tractor.tractormusic.model;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.gnapse.tractor.tractormusic.musicplayer.MusicPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MusicRepository {
    public static final String TAG = MusicRepository.class.getSimpleName();

    private Application mApplication;
    private static MusicRepository sInstance;

    private ArrayList<MusicItem> mMusicItemList = new ArrayList<MusicItem>();
    private ArrayList<String> mDurationList = new ArrayList<String>();

    private MutableLiveData<MusicItem> mMusicItem = new MutableLiveData<>();
    private MutableLiveData<String> mMusicTitle = new MutableLiveData<>();
    private MutableLiveData<String> mMusicArtist = new MutableLiveData<>();
    private MutableLiveData<String> mMusicDuration = new MutableLiveData<>();
    private MutableLiveData<Integer> mMusicRepeatMode = new MutableLiveData<>();
    private MutableLiveData<Uri> mAlbutmArtUri = new MutableLiveData<>();

    private MusicRepository(Application application) {
        this.mApplication = application;
    }

    public static MusicRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (MusicRepository.class) {
                if (sInstance == null) {
                    sInstance = new MusicRepository(application);
                }
            }
        }
        return sInstance;
    }

    public ArrayList<MusicItem> getMusicItemList() { return mMusicItemList; }

    public MutableLiveData<MusicItem> getMusicItem() { return mMusicItem; }

    public MutableLiveData<String> getMusicTitle() {
        return mMusicTitle;
    }

    public MutableLiveData<String> getMusicArtist() {
        return mMusicArtist;
    }

    public MutableLiveData<String> getMusicDuration() {
        return mMusicDuration;
    }

    public MutableLiveData<Integer> getMusicRepeatMode() {
        return mMusicRepeatMode;
    }

    public MutableLiveData<Uri> getAlbumArtUri() {
        return mAlbutmArtUri;
    }

    public void setMusicTitle(String title) {
        mMusicTitle.setValue(title);
    }

    public void setMusicArtist(String artist) {
        mMusicArtist.setValue(artist);
    }

    public void setMusicDuration(String duration) {
        mMusicDuration.setValue(duration);
    }

    public void setRepeatMode(int mode) {
        mMusicRepeatMode.setValue(mode);
    }

    public void setAlbumArtUri(Uri uri) {
        mAlbutmArtUri.setValue(uri);
    }

    public ArrayList<MusicItem> getShuffledList() {
        Collections.shuffle(mMusicItemList, new Random());
        return mMusicItemList;
    }

    public void getExternalMusicData() {
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = "lower(" + MediaStore.Audio.Media.TITLE + ") ASC";
        ArrayList<MusicItem> musicItemList = new ArrayList<MusicItem>();
        ArrayList<String> durationList = new ArrayList<String>();

        String[] what = new String[]{
                MediaStore.Audio.Media._ID, // 고유 ID
                MediaStore.Audio.Media.TITLE, // 제목
                MediaStore.Audio.Media.ARTIST, // 가수
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Albums.ALBUM_ID
        };

        try (Cursor cursor = mApplication.getContentResolver().query(
                externalUri,
                what,
                null,
                null,
                sortOrder)){

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int artColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                long albumId = cursor.getLong(artColumn);
                String duration = cursor.getString(durationColumn);

                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri contentUri = ContentUris.withAppendedId(externalUri, id);
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                durationList.add(duration);
                musicItemList.add(new MusicItem(contentUri, title, artist, albumArtUri, duration));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        mMusicItemList = musicItemList;
        mDurationList = durationList;
    }

    public String convertDuration() {
        String duration;
        String stringDuration = mMusicItemList.get(MusicPlayer.getInstance(mApplication).getCurrentPlayIndex()).getDuration();
        int min = Integer.parseInt(stringDuration)/1000/60;
        int sec = Integer.parseInt(stringDuration)/1000%60;

        if (sec <= 9) {
            duration = "0" + min + ":0" + sec;
        } else {
            duration = "0" + min + ":" + sec;
        }

        return duration;
    }
}
