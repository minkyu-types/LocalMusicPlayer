package com.gnapse.tractor.tractormusic.viewmodel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.gnapse.tractor.tractormusic.model.MusicItem;
import com.gnapse.tractor.tractormusic.model.MusicRepository;
import com.gnapse.tractor.tractormusic.model.PlayStateRepository;
import java.util.ArrayList;

public class MusicPlayerViewModel extends BaseViewModel {
    private MusicRepository mMusicRepository;

    private LiveData<MusicItem> mMusicItem = new MutableLiveData<>();
    private LiveData<String> mMusicTitle = new MutableLiveData<>();
    private LiveData<String> mMusicArtist = new MutableLiveData<>();;
    private LiveData<String> mMusicDuration = new MutableLiveData<>();
    private LiveData<Integer> mMusicRepeatMode = new MutableLiveData<>();
    private LiveData<Uri> mAlbumArtUri = new MutableLiveData<>();

    public MusicPlayerViewModel(@NonNull Application application) {
        super(application);
        mMusicRepository = MusicRepository.getInstance(application);
    }

    public LiveData<String> getMusicTitle() {
        return mMusicTitle = Transformations.map(mMusicRepository.getMusicTitle(), input -> input);
    }

    public LiveData<String> getMusicArtist() {
        return mMusicArtist = Transformations.map(mMusicRepository.getMusicArtist(), input -> input );
    }

    public LiveData<String> getDuration() {
        return mMusicDuration = Transformations.map(mMusicRepository.getMusicDuration(), input -> input);
    }

    public LiveData<Integer> getMusicRepeatMode() {
        return mMusicRepeatMode = Transformations.map(mMusicRepository.getMusicRepeatMode(), input -> input);
    }

    public LiveData<Uri> getAlbumArtUri() {
        return mAlbumArtUri = Transformations.map(mMusicRepository.getAlbumArtUri(), input -> input);
    }
}