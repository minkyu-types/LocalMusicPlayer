package com.gnapse.tractor.tractormusic.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.gnapse.tractor.tractormusic.viewmodel.MusicListViewModel;
import com.gnapse.tractor.tractormusic.viewmodel.MusicPlayerViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    public ViewModelFactory(Application mApplication) {
        this.mApplication = mApplication;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MusicPlayerViewModel.class) {
            return (T) new MusicPlayerViewModel(mApplication);
        } else if (modelClass == MusicListViewModel.class) {
            return (T) new MusicListViewModel(mApplication);
        }
        return null;
    }
}
