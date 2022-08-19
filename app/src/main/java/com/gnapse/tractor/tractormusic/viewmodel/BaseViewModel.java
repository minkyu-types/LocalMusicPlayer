package com.gnapse.tractor.tractormusic.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;

public abstract class BaseViewModel extends AndroidViewModel {
    private LifecycleOwner mLifecycleOwner;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    public LifecycleOwner getLifecycleOwner() {
        return mLifecycleOwner;
    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onViewFocused() {

    }

    public void onViewLostFocus() {

    }

    public void observeViewModel() {

    }

    public void reserveViewModel() {

    }
}
