package com.gnapse.tractor.tractormusic.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.gnapse.tractor.tractormusic.model.MusicItem;
import com.gnapse.tractor.tractormusic.model.MusicRepository;
import java.util.ArrayList;

public class MusicListViewModel extends BaseViewModel {
    private MusicRepository mMusicRepository;

    private ArrayList<MusicItem> mMusicList = new ArrayList<MusicItem>();
    private LiveData<String> mMusicTitle = new MutableLiveData<>();
    private LiveData<String> mMusicArtist = new MutableLiveData<>();;

    public MusicListViewModel(@NonNull Application application) {
        super(application);
        mMusicRepository = MusicRepository.getInstance(application);
    }

    public ArrayList<MusicItem> getMusicList() {
        return mMusicList = mMusicRepository.getMusicItemList();
    }

    public LiveData<String> getMusicTitle() {
        return mMusicTitle = Transformations.map(mMusicRepository.getMusicTitle(), input -> input);
    }

    public LiveData<String> getMusicArtist() {
        return mMusicArtist = Transformations.map(mMusicRepository.getMusicArtist(), input -> input );
    }
}
